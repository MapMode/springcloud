package rs485.socket.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import org.apache.http.util.TextUtils;

import java.net.InetSocketAddress;

public class NettyClient {

    private final String host;
    private final int port;
    private Channel channel;

    //连接服务端的端口号地址和端口号
    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start()  {
        final EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)  // 使用NioSocketChannel来作为连接用的channel类
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() { // 绑定连接初始化器
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        System.out.println("正在连接中...");
                        ChannelPipeline pipeline = ch.pipeline();
                        //pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                        //pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
                        // 编码request
                        //pipeline.addLast("decoder", new StringDecoder());
                        //解码response
                        //pipeline.addLast("encoder", new StringEncoder());
                        //使用了netty自带的编码器和解码器
                        pipeline.addLast(new ByteArrayEncoder());
                        //处理类
                        pipeline.addLast("handler", new ClientHandler());

                    }
                });
        try{
            //建立连接
            ChannelFuture f = b.connect(host, port).sync();
            //byte[] dd = Crc16Util.getData("01", "03","00","00","00","07");

            InetSocketAddress socketAddress = (InetSocketAddress) f.channel().localAddress();
            System.out.print(String.format("Client port:{%s}", socketAddress.getPort()));

            //等待服务监听端口关闭
            f.channel().closeFuture().sync();
        }catch (Exception e){
            System.out.print("netty服务异常了:{}");
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
            System.out.print("netty服务端断开了链接:" + Thread.currentThread().getId());
        }

        //发起异步连接请求，绑定连接端口和host信息
        //final ChannelFuture future = b.connect(host, port).sync();

        /*future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture arg0) throws Exception {
                if (future.isSuccess()) {
                    System.out.println("连接服务器成功");
                } else {
                    System.out.println("连接服务器失败");
                    future.cause().printStackTrace();
                    group.shutdownGracefully(); //关闭线程组
                }
            }
        });*/

        //this.channel = future.channel();
    }

    public Channel getChannel() {
        return channel;
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
