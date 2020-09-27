package rs485.socket.client;


/*
 * Client的消息处理类Handler，继承SimpleChannelInboundHandler
 */

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.http.util.TextUtils;
import rs485.util.Crc16Util;

public class ClientHandler extends ChannelInboundHandlerAdapter {
 
    //处理服务端返回的数据
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("接受到server响应数据: " + msg);

        ByteBuf buf = (ByteBuf)msg;
        byte [] bytes = new byte[buf.readableBytes()];
        //复制内容到字节数组bytes
        buf.readBytes(bytes);

        if(bytes.length >= 1){
            String s = BinaryToHexString(bytes);
            //01 03 0E 00 CB 00 01 00 01 01 2C 80 7A 00 C3 00 EB A7 84
            String[] s1 = s.split(" ");

            System.out.println(s);
        }else{
            System.out.println("为获取到数据");
        }
        //将接收到的数据转为字符串，此字符串就是服务端发送的字符串
        //String receiveStr = ConvertCode.receiveHexToString(bytes);
        //ctx.writeAndFlush(receiveStr);
        //返回16进制到客户端
        //writeToClient(receiveStr,channel,"测试");
        //long dec_num = Long.parseLong(body, 16);
        //MyTools myTools=new MyTools();
        //myTools.writeToClient("1002050a840336010102328002021003",ctx,"测试");
    }
 
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("正在连接... ");

        //发送消息
        byte[] dd = Crc16Util.getData("01", "03","00","00","00","07");
        ctx.channel().writeAndFlush(dd);
        //super.channelActive(ctx);
    }
 
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("连接关闭! ");
        ctx.close();
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
        final String hexStr = "0123456789ABCDEF";
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