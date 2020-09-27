package rs485.tcpSocket;

import org.apache.http.util.TextUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

@Controller
@RequestMapping("/haolida")
public class HttpClientController {

    @GetMapping("/list")
    @ResponseBody
    public String getList() {
        String cmdInfor = "01 03 00 65 00 04 54 16";
        String send = send(cmdInfor);
        return send;
    }

    public static void main(String args[]){
        String cmdInfor = "01 03 00 00 00 07 04 08";
        //String cmdInfor = "01 03 00 00 00 07";
        //byte[] dd = Crc16Util.getData("01", "03","00","00","00","07")
        String send = send(cmdInfor);
        System.out.println("send"+send);
    }

    public static String send(String cmdInfor) {
        String strReturn = null;
        try {
            //要连接的服务端IP地址
            String host = "10.3.155.222";
            //要连接的服务端对应的监听端口
            int port = 10001;
            //将十六进制的字符串转换成字节数组
            byte[] cmdInfor2 = hexStrToBinaryStr(cmdInfor);
            //byte[] cmdInfor2 = Crc16Util.getData("01", "03","00","00","00","07");

            //1.建立客户端socket连接，指定服务器位置及端口
            Socket clientSocket = new Socket(host, port);

            //2.得到socket读写流
            OutputStream os = clientSocket.getOutputStream();
            PrintWriter pw = new PrintWriter(os);
            //输入流
            InputStream is = clientSocket.getInputStream();

            //3.利用流按照一定的操作，对socket进行读写操作
            os.write(cmdInfor2);
            os.flush();
            clientSocket.shutdownOutput();

            //接收服务器的响应
            int line = 0;
            byte[] buf = new byte[is.available()];

            //接收收到的数据
            if(buf.length >= 1){
                while ((line = is.read(buf)) != -1) {
                    //将字节数组转换成十六进制的字符串
                    strReturn = BinaryToHexString(buf);
                }
                System.out.println("strReturn"+strReturn);
            }else{
                System.out.println("未获取到值");
            }
            //4.关闭资源
            is.close();
            pw.close();
            os.close();
            clientSocket.close();
//            clientSocket.shutdownOutput();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strReturn;
    }

    /**
     * 将十六进制的字符串转换成字节数组
     *
     * @param hexString
     * @return
     */
    public static byte[] hexStrToBinaryStr(String hexString) {
        if (TextUtils.isEmpty(hexString)) {
            return null;
        }

        hexString = hexString.replaceAll(" ", "");
        int len = hexString.length();
        int index = 0;
        byte[] bytes = new byte[len / 2];

        while (index < len) {
            String sub = hexString.substring(index, index + 2);
            bytes[index / 2] = (byte) Integer.parseInt(sub, 16);
            index += 2;
        }
        return bytes;
    }

    /**
     * 将字节数组转换成十六进制的字符串
     *
     * @return
     */
    public static String BinaryToHexString(byte[] bytes) {
        String hexStr = "0123456789ABCDEF";
        String result = "";
        String hex = "";
        for (byte b : bytes) {
            hex = String.valueOf(hexStr.charAt((b & 0xF0) >> 4));
            hex += String.valueOf(hexStr.charAt(b & 0x0F));
            result += hex + " ";
        }
        return result;
    }

}
