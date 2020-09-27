package rs485.socket.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.http.util.TextUtils;

/*
 * 编码器（将实体类转换成可传输的数据）
 */
public class RpcEncoder extends MessageToByteEncoder {

    /**
     * 编码
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        //System.out.println(msg);
        /*if (target.isInstance(msg)) {
            byte[] data = JSON.toJSONBytes(msg); //使用fastJson将对象转换为byte
        }*/
        //byte[] bytes;
        //if(msg instanceof String){
        //    bytes = hexStrToBinaryStr((String)msg);
            //先将消息长度写入，也就是消息头
        //    out.writeInt(bytes.length);
            //消息体中包含我们要发送的数据
        //    out.writeBytes(bytes);
        //}
    }

    /**
     * 将十六进制的字符串转换成字节数组
     * @param hexString 字符串
     * @return 数组
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
 
}