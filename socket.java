
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class socket{

    public static void main(String[] args) throws IOException {
        int port = 5678;
        ServerSocket serverSocket = new ServerSocket(port);
        System.err.println("Server is running on port: "+port);
        String path1 = "/users";
        String path2 = "/adduser";
        String path3 = "/deleteuser";
        ArrayList<String> users = new ArrayList<>();
        users.add("Heisenberg");
        users.add("Gustavo Fring");
        users.add("Tuco");
        users.add("Hector");
        users.add("Jay");
        while(true){
            Socket clientSocket = serverSocket.accept();
            System.err.println("Client connected");
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            ArrayList<String> responce = new ArrayList<>();
            String s;
            while((s = in.readLine())!=null) {
                System.out.println(s);
                responce.add(s);
                if (s.isEmpty()) {
                    break;
                }
            }
            OutputStream out = clientSocket.getOutputStream();
            String geturl="";

            for(String temp:responce){
                if(temp.contains("GET /")){
                    System.out.println("url found: "+temp);
                    geturl = temp;
                    break;
                }
            }
            System.out.println("URL "+geturl );

            String[] temp2 = geturl.split(" ");
         /*   for(String i:temp2){
                System.out.println(i);
            }*/

            String action = temp2[1];
            System.out.println("Action:  "+ geturl.length());
            String[] extractinput = new String[2];
            String addusername = "";
            String deleteusername = "";
            if(action.contains("?adduser=")){

                extractinput = action.split("=");
                System.out.println("Extacted Add USer input from Action :   " +extractinput[0] + " "+extractinput[1]);
                addusername = extractinput[1];
                if(!users.contains(addusername)) {
                    users.add(addusername);
                    System.out.println("User name added successfully. Check it out in users list page");
                }else{
                    System.out.println("Already a user. Please enter a new user name");
                }


            }

            if(action.contains("?deleteuser=")){
                extractinput = action.split("=");
                System.out.println("Extacted Delete USer input from Action:   " +extractinput[0] + " "+extractinput[1]);
                deleteusername = extractinput[1];
                int index = users.indexOf(deleteusername);
                if(index>-1) {
                    users.remove(index);
                    System.out.println("User name deleted! Check it out in users list page");
                }else{
                    System.out.println("Please enter a valid user name");
                }
            }


            String html_start = "<!DOCTYPE html> <html> <head> <meta charset=\"UTF-8\"> </head> <body>";
            String html_end = "</body> </html>";

            if(action.equals("/users")){
                out.flush();
                System.out.println("USERS Page");
                out.write("HTTP/1.1 200 OK\r\n".getBytes());
                out.write("\r\n".getBytes());
                out.write("<h1>List of Users</h1> <ul>".getBytes());
                for(int i =0;i<users.size();i++){
                    out.write(("<p><li>"+users.get(i)+"</li></p>").getBytes());
                }
                out.write("</ul>".getBytes());
                out.write("\r\n\r\n".getBytes());
             //   out.flush();
            }else if(action.equals("/adduser")){
                out.flush();
                System.out.println("ADD USER Page");
                out.write("HTTP/1.1 200 OK\r\n".getBytes());
                out.write("\r\n".getBytes());
                out.write(html_start.getBytes());
                out.write("<h1>Add User Page</h1>".getBytes());
                out.write(" <form method = \"get\"> ".getBytes());
                out.write("<h4> Enter user to be added </h4>".getBytes());
                out.write("<input id=\"adduser\" name = \"adduser\" type = \"text\">".getBytes());
                out.write("<p><input type = \"submit\"></p>".getBytes());
                out.write("</form>".getBytes());
                out.write(html_end.getBytes());
                out.write("\r\n\r\n".getBytes());
               // out.flush();
            }
            else if(action.equals("/deleteuser")){
                out.flush();
                System.out.println("DELETE USER Page");
                out.write("HTTP/1.1 200 OK\r\n".getBytes());
                out.write("\r\n".getBytes());
                out.write("<h1>Remove Page</h1>".getBytes());
                out.write(" <form method = \"get\"> ".getBytes());
                out.write("<h4> Enter user to be removed </h4>".getBytes());
                out.write("<input id=\"deleteuser\" name = \"deleteuser\" type = \"text\">".getBytes());
                out.write("<p><input type = \"submit\"></p>".getBytes());
                out.write("</form>".getBytes());
                out.write("\r\n\r\n".getBytes());
             //   out.flush();
            }else {
                out.write("HTTP/1.1 200 OK\r\n".getBytes());
                out.write("\r\n".getBytes());
                out.write("<b>Menu</b>".getBytes());
                out.write(("<p><ul><li><a href =\"" + path1 + "\" > List Users</a></li><li><a href=\"" + path2 + "\" > Add User</a></li><li><a href = \"" + path3 + "\" > Remove User</a></li></ul></p>").getBytes());
                out.write("\r\n\r\n".getBytes());
              //  out.flush();
            }
            out.flush();
            System.err.println("Client connection closed!");
            out.close();
            in.close();
        }
    }
}