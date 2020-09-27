package rs485.socket.client;

import rs485.util.ConvertCode;

import java.text.DecimalFormat;

/**
 */
public class Main {
 
    public static void main(String[] args) throws Exception {
        //NettyClient client = new NettyClient("10.3.155.222", 10001);
        //NettyClient client = new NettyClient("127.0.0.1", 8888);
        //启动client服务
        //client.start();
 
        //Channel channel = client.getChannel();

        //channel对象可保存在map中，供其它地方发送消息

        //String req = "01 03 00 00 00 07";
        //byte[] dd = Crc16Util.getData("01", "03","00","00","00","07");
        //向服务端发送的消息
        //channel.writeAndFlush(dd);

        //解析数据
        //01 03 0E 00 CB 00 01 00 01 01 2C 80 7A 00 C3 00 EB A7 84
        analysis("01 03 0E 00 CB 00 01 00 01 01 2C 80 7A 00 C3 00 EB A7 84");
    }


    public static void analysis(String msg){
        String[] split = msg.split(" ");
        if(split.length > 5){
            //探测器地址
            String detector_address = split[0];
            //读/写数据
            String operation_data = split[1];
            //返回数据长度(字节)
            String return_length = split[2];
            //CRC校验高8位
            String crc_height = split[split.length - 2];
            //CRC校验低8位
            String rcr_low = split[split.length - 1];
            //浓度值
            int concentrationValue = 0;
            //探测器状态
            int detectorStatus = 0;
            //小数点
            int decimal = 0;
            //量程
            int range = 0;
            //ADC值
            int ADC_value = 0;
            //低报值
            int underreport = 0;
            //高报值
            int higher = 0;
            //从第三个开始, 倒数第三个结束
            for (int i = 3; i <= split.length-3; i++){
                // 探测器状态高8位 无状态 / 小数点个数高 8 位 无状态
                if(i == 5 || i == 7){
                    continue;
                }

                String get_value = split[i];
                int outcome = ConvertCode.transformationToTen(get_value);
                if(i == 3){
                    //浓度数据高8位
                    concentrationValue = outcome;
                    continue;
                }
                if(i == 4){
                    //浓度数据低8位
                    concentrationValue = concentrationValue * 255 + outcome;
                    continue;
                }

                if(i == 6){
                    //探测器状态低8位
                    //0-预热， 1-正常， 3-故障， 5-低报， 6 高报
                    detectorStatus = outcome;
                    continue;
                }


                if(i == 8){
                    //小数点个数低 8 位
                    decimal = outcome * 10;
                    continue;
                }
                if(i == 9){
                    //量程高 8 位
                    range = outcome;
                    continue;
                }
                if(i == 10){
                    //量程低 8 位
                    range = range * 255 + outcome;
                    continue;
                }
                if(i == 11){
                    //ADC 值高 8 位
                    ADC_value = outcome;
                    continue;
                }
                if(i == 12){
                    //ADC 值低 8 位
                    ADC_value = ADC_value * 255 + outcome;
                    continue;
                }
                if(i == 13){
                    //低报值高 8 位
                    underreport = outcome;
                    continue;
                }
                if(i == 14){
                    //低报值低 8 位
                    underreport = underreport * 255 + outcome;
                    continue;
                }
                if(i == 15){
                    //高报值低 8 位
                    higher = outcome;
                    continue;
                }
                if(i == 16){
                    //高报值低 8 位
                    higher = higher * 255 + outcome;
                    continue;
                }
            }


            DecimalFormat df=new DecimalFormat("0.00");
            String str_concentrationValue = df.format((float)concentrationValue / decimal);

            System.out.println(str_concentrationValue+"---"+detectorStatus+"---"+range+"---"+ADC_value+"---"+underreport+"---"+higher);
        }
    }

}