package security;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * 常用数字签名算法DSA
 *
 */
public class DSACode {

    private static final long serialVersionUID = 1L;


    // 公钥
    private static final String PUBLIC_KEY = "DSAPublicKey";

    // 私钥
    private static final String PRIVATE_KEY = "DSAPrivateKey";

    private static final String privatekeyFile = "/Users/cuixiaodong/testprivate.key";

    private static final String publickeyFile = "/Users/cuixiaodong/testpubkey.key";

    private static PrivateKey privateKey;

    private static PublicKey publicKey;

    private static BASE64Decoder base64decoder = new BASE64Decoder();

    private static BASE64Encoder base64encoder = new BASE64Encoder();

    private static Map<String, Object> keyMap = new HashMap<String, Object>();

    private static String path = "";


    /**
     * 初始化密钥对
     *
     * @return Map 甲方密钥的Map
     * */
    public static Map<String, Object> initKey() throws Exception {
        initPrivateKey();
        initPublicKey();
        // 将密钥存储在map中
        keyMap = new HashMap<String, Object>();
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);

        return keyMap;

    }

    /**
     * 取得私钥
     *
     * @param keyMap
     *            密钥map
     * @return byte[] 私钥
     * */
    public static byte[] getPrivateKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return key.getEncoded();
    }

    /**
     * 取得公钥
     *
     * @param keyMap
     *            密钥map
     * @return byte[] 公钥
     * */
    public static byte[] getPublicKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return key.getEncoded();
    }

    /**
     * 读取私钥
     */
    private static void initPrivateKey() {
        try {
            /*BufferedReader br = new BufferedReader(new PrivateKeyReader(path
                    + privatekeyFile));
            String s = br.readLine();
            StringBuffer privatekey = new StringBuffer();
            s = br.readLine();
            while (s.charAt(0) != '-') {
                privatekey.append(s + "\r");
                s = br.readLine();
            }*/

            /*byte[] keybyte = base64decoder.decodeBuffer();

            KeyFactory kf = KeyFactory.getInstance("RSA");

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keybyte);

            privateKey = kf.generatePrivate(keySpec);*/
            privateKey=new PrivateKeyReader(path+ privatekeyFile).getPrivateKey();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 读取公钥
     */
    private static void initPublicKey() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(path
                    + publickeyFile));
            String s = br.readLine();
            StringBuffer publickey = new StringBuffer();
            s = br.readLine();
            while (s.charAt(0) != '-') {
                publickey.append(s + "\r");
                s = br.readLine();
            }

            byte[] keybyte = base64decoder.decodeBuffer(publickey.toString());

            KeyFactory kf = KeyFactory.getInstance("RSA");

            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keybyte);

            publicKey = kf.generatePublic(keySpec);
        } catch (Exception e) {
        }
    }

    /**
     * 签名
     *
     * @param content
     *            待签名字符串 私钥加密
     * @return
     */
    public static String sign(String content) {
        try {
            Signature signalg = Signature.getInstance("NONEwithRSA");
            signalg.initSign(privateKey);
            signalg.update(content.getBytes());

            byte[] signature = signalg.sign();

            String sign = base64encoder.encode(signature);
            return sign;
        } catch (Exception e) {
        }
        return "";

    }

    /**
     * 数字校验签名
     *
     * @param signature
     *            签名串
     * @param contecnt
     *            待校验内容  公钥揭秘
     * @return
     */
    public static boolean verify(String signature, String contecnt) {
        try {
//            Signature verifyalg = Signature.getInstance("DSA");
            Signature verifyalg = Signature.getInstance("NONEwithRSA");
            verifyalg.initVerify(publicKey);

            verifyalg.update(contecnt.getBytes());
            byte[] signbyte = base64decoder.decodeBuffer(signature);
            return verifyalg.verify(signbyte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) throws Exception {
        // 初始化密钥
        // 生成密钥对
        Map<String, Object> keyMap;
        try {
            keyMap = DSACode.initKey();
            // 公钥
            byte[] publicKey = DSACode.getPublicKey(keyMap);
            // 私钥
            byte[] privateKey = DSACode.getPrivateKey(keyMap);

            System.out.println("公钥：" + base64encoder.encodeBuffer(publicKey));
            System.out.println("私钥：" + base64encoder.encodeBuffer(privateKey));
        } catch (Exception e) {

        }


        String str = "<test></test>";
        System.out.println("原文:" + str);
//加密
        byte[] cipher = new RSAUtil().encrypt(publicKey, str.getBytes());
        //解密
        byte[] plainText = new RSAUtil().decrypt(privateKey, cipher);
        System.out.println("密文长度:"+ cipher.length);
        System.out.println(RSAUtil.byteArrayToString(cipher));
        System.out.println("明文长度:"+ plainText.length);
        System.out.println(new String(plainText));

        // 甲方进行数据的加密
        String sign = sign(str);
        System.out.println("产生签名：" + sign);
        // 验证签名
        boolean status = verify(sign, str);
        System.out.println("状态：" + status);
    }
}