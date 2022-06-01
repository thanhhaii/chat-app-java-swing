package util;


import dao.AccountDao;
import dao.MessageDao;
import entity.Account;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    private Object lock;
    private ServerSocket s;
    private Socket socket;
    static ArrayList<Handler> clients = new ArrayList<Handler>();
    private String username;

    public Server() throws Exception{
        try {
            lock = new Object();
            s = new ServerSocket(1411);

            while (true){
                socket = s.accept();
                var dis = new DataInputStream(socket.getInputStream());
                var dos = new DataOutputStream(socket.getOutputStream());
                username = dis.readUTF();
                var newHandler = new Handler(socket, username, lock);
                clients.add(newHandler);
                var t = new Thread(newHandler);
                t.start();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

class Handler implements Runnable{

    private final Object lock;

    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private String username;

    public Handler(Socket socket, String username, Object lock) throws IOException {
        this.socket = socket;
        this.username = username;
        this.dis = new DataInputStream(socket.getInputStream());
        this.dos = new DataOutputStream(socket.getOutputStream());
        this.lock = lock;
    }

    public Handler(String username, Object lock, Socket socket) {
        this.username = username;
        this.lock = lock;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
        try {
            this.dis = new DataInputStream(socket.getInputStream());
            this.dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeSocket(){
        if(socket != null){
            try{
                socket.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public DataOutputStream getDos() {
        return dos;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void run() {

        loop: while (true) {
            try {
                String message = null;
                var messageDao = new MessageDao();
                message = dis.readUTF();
                switchLoop: switch (message) {
                    case "Text" -> {
                        var receiver = dis.readUTF();
                        var content = dis.readUTF();
                        for (Handler client : Server.clients) {
                            if (client.getUsername().equals(receiver)) {
                                synchronized (lock) {
                                    client.getDos().writeUTF("Text");
                                    client.getDos().writeUTF(this.username);
                                    client.getDos().writeUTF(content);
                                    client.getDos().flush();
                                    break;
                                }
                            }else {
                                messageDao.seenMessage(receiver, false);
                            }
                        }
                    }
                    case "TextGroup" -> {
                        String roomId = dis.readUTF();
                        String content = dis.readUTF();
                        for(Handler client : Server.clients){
                            new AccountDao().getMemberGroupChat(Integer.parseInt(roomId)).forEach(user -> {
                                if(client.getUsername().equals(user.getUsername())){
                                    synchronized (lock){
                                        try {
                                            client.getDos().writeUTF("TextGroup");
                                            client.getDos().writeUTF(this.username);
                                            client.getDos().writeUTF(roomId);
                                            client.getDos().writeUTF(content);
                                            client.getDos().flush();
                                        } catch (IOException ioException) {
                                            ioException.printStackTrace();
                                        }
                                    }
                                }
                            });
                        }
                    }
                    case "Log out" -> {
                        dos.writeUTF("Log out");
                        dos.flush();
                        closeSocket();
                        Server.clients.removeIf(client -> client.getUsername().equals(this.username));
                        break loop;
                    }
                    case "Emoji" -> {
                        String receiver = dis.readUTF();
                        String emoji = dis.readUTF();
                        for (Handler client : Server.clients) {
                            if (client.getUsername().equals(receiver)) {
                                synchronized (lock) {
                                    client.getDos().writeUTF("Emoji");
                                    client.getDos().writeUTF(this.username);
                                    client.getDos().writeUTF(emoji);
                                    client.getDos().flush();
                                    break;
                                }
                            }
                        }
                    }
                    case "EmojiGroup" -> {
                        String roomId = dis.readUTF();
                        String emoji = dis.readUTF();
                        for(Handler client : Server.clients){
                            new AccountDao().getMemberGroupChat(Integer.parseInt(roomId)).forEach(user -> {
                                if(client.getUsername().equals(user.getUsername())){
                                    synchronized (lock){
                                        try {
                                            client.getDos().writeUTF("EmojiGroup");
                                            client.getDos().writeUTF(this.username);
                                            client.getDos().writeUTF(roomId);
                                            client.getDos().writeUTF(emoji);
                                            client.getDos().flush();
                                        } catch (IOException ioException) {
                                            ioException.printStackTrace();
                                        }
                                    }
                                }
                            });
                        }
                    }
                    case "File" -> {

                        // Đọc các header của tin nhắn gửi file
                        String receiver = dis.readUTF();
                        String filename = dis.readUTF();
                        int size = Integer.parseInt(dis.readUTF());
                        int bufferSize = 2048;
                        byte[] buffer = new byte[bufferSize];

                        for (Handler client : Server.clients) {
                            if (client.getUsername().equals(receiver)) {
                                synchronized (lock) {
                                    client.getDos().writeUTF("File");
                                    client.getDos().writeUTF(this.username);
                                    client.getDos().writeUTF(filename);
                                    client.getDos().writeUTF(String.valueOf(size));
                                    while (size > 0) {
                                        // Gửi lần lượt từng buffer cho người nhận cho đến khi hết file
                                        dis.read(buffer, 0, Math.min(size, bufferSize));
                                        client.getDos().write(buffer, 0, Math.min(size, bufferSize));
                                        size -= bufferSize;
                                    }
                                    client.getDos().flush();
                                    break;
                                }
                            }
                        }
                    }
                    case "FileGroup" -> {
                        String roomId = dis.readUTF();
                        String fileName = dis.readUTF();
                        String fileLength = dis.readUTF();
                        int size = Integer.parseInt(fileLength);
                        int bufferSize = 2048;
                        byte[] buffer = new byte[bufferSize];
                        var list = new ArrayList<Account>();
                        new AccountDao().getMemberGroupChat(Integer.parseInt(roomId)).forEach(user -> {
                            list.add(new Account(user.getUsername()));
                        });
                        for(Handler client : Server.clients){
                            for (Account account : list) {
                                if (client.getUsername().equals(account.getUsername())) {
                                    synchronized (lock) {
                                        client.getDos().writeUTF("FileGroup");
                                        client.getDos().writeUTF(this.username);
                                        client.getDos().writeUTF(roomId);
                                        client.getDos().writeUTF(fileName);
                                        client.getDos().writeUTF(String.valueOf(size));
                                        while (size > 0) {
                                            // Gửi lần lượt từng buffer cho người nhận cho đến khi hết file
                                            dis.read(buffer, 0, Math.min(size, bufferSize));
                                            client.getDos().write(buffer, 0, Math.min(size, bufferSize));
                                            size -= bufferSize;
                                        }
                                        client.getDos().flush();
                                        break;
                                    }
                                }
                            }
                        }
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
