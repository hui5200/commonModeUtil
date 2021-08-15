package com.ailin.util;

import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.asn1.pkcs.Attribute;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;

import javax.crypto.Cipher;
import javax.security.auth.x500.X500Principal;
import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA加密解密
 * 备注：
 * 约定双方的BASE64编码
 * 约定双方分段加解密的方式。
 * @author Administrator
 *
 */
public class RSA2Utils {
	
	public static final String CHARSET = "UTF-8";
	public static final String RSA_ALGORITHM = "RSA";
	private static final int KEY_SIZE = 2048;//设置长度

	public static final String PRIVATE_KEY = "privateKey";
	public static final String PUBLIC_KEY = "publicKey";

	public static Map<String, String> createKeys(){
		// 为RSA算法创建一个KeyPairGenerator对象
		KeyPairGenerator kpg;
		try {
			kpg = KeyPairGenerator.getInstance(RSA_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException("No such algorithm-->[" + RSA_ALGORITHM + "]");
		}
		// 初始化KeyPairGenerator对象,密钥长度
		kpg.initialize(KEY_SIZE);
		// 生成密匙对 
		KeyPair keyPair = kpg.generateKeyPair();
		// 得到公钥
		Key publicKey = keyPair.getPublic();
		String publicKeyStr = Base64.encodeBase64URLSafeString(publicKey.getEncoded());
		// 得到私钥
		Key privateKey = keyPair.getPrivate();
		String privateKeyStr = Base64.encodeBase64URLSafeString(privateKey.getEncoded());
		Map<String, String> keyPairMap = new HashMap<String, String>();
		keyPairMap.put(PUBLIC_KEY, publicKeyStr);
		keyPairMap.put(PRIVATE_KEY, privateKeyStr);
		return keyPairMap;
	}

	/**
	}
 
     * 得到公钥
     * @param publicKeyString 密钥字符串（经过base64编码）
     * @throws Exception
     */
	public static RSAPublicKey getPublicKey(String publicKeyString) throws NoSuchAlgorithmException, InvalidKeySpecException, CertificateException {
		// 通过X509编码的Key指令获得公钥对象
		publicKeyString = publicKeyString.substring(29,publicKeyString.length()-29);
		byte[] pkey = Base64.decodeBase64(publicKeyString);
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		X509Certificate x509Certificate = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(pkey));
		return (RSAPublicKey)x509Certificate.getPublicKey();
	}

	/**
	}
     * 得到私钥
     * @param privateKeyString 密钥字符串（经过base64编码）
     * @throws Exception
     */ 
	public static RSAPrivateKey getPrivateKey(String privateKeyString)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		// 通过PKCS#8编码的Key指令获得私钥对象
		privateKeyString = privateKeyString.substring(28,privateKeyString.length()-26);
		KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyString));
		return  (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
	}

	/**
	}
     * 公钥加密
     * @param data
     * @param publicKey
     * @return
     */ 
	public static String publicEncrypt(String data, RSAPublicKey publicKey) {
		try {
			Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return Base64.encodeBase64URLSafeString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET),
					publicKey.getModulus().bitLength()));
		} catch (Exception e) {
			throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
		}
	}

	/**
     * 私钥解密
     * @param data
     * @param privateKey
     * @return
     */
	public static String privateDecrypt(String data, RSAPrivateKey privateKey) {
		try {
			Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data),
					privateKey.getModulus().bitLength()), CHARSET);
		} catch (Exception e) {
			throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
		}
	}

	/**
	 * 私钥加密
	 * 
	 * @param data
	 * @param privateKey
	 * @return
	 */
	public static String privateEncrypt(String data, RSAPrivateKey privateKey) {
		try {
			Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			return Base64.encodeBase64URLSafeString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET),
					privateKey.getModulus().bitLength()));
		} catch (Exception e) {
			throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
		}
	}

	/**
	 * 公钥解密
	 * 
	 * @param data
	 * @param publicKey
	 * @return
	 */
	public static String publicDecrypt(String data, RSAPublicKey publicKey) {
		try {
			Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data),
					publicKey.getModulus().bitLength()), CHARSET);
		} catch (Exception e) {
			throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
		}
	}

	private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize) {
		int maxBlock = 0;
		if (opmode == Cipher.DECRYPT_MODE) {
			maxBlock = keySize / 8;
		} else {
			maxBlock = keySize / 8 - 11;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] buff;
		int i = 0;
		try {
			while (datas.length > offSet) {
				if (datas.length - offSet > maxBlock) {
					buff = cipher.doFinal(datas, offSet, maxBlock);
				} else {
					buff = cipher.doFinal(datas, offSet, datas.length - offSet);
				}
				out.write(buff, 0, buff.length);
				i++;
				offSet = i * maxBlock;
			}
		} catch (Exception e) {
			throw new RuntimeException("加解密阀值为[" + maxBlock + "]的数据时发生异常", e);
		}
		byte[] resultDatas = out.toByteArray();
		IOUtils.closeQuietly(out);
		return resultDatas;
	}
	/**
	 * 证书生成
	 */
	public static Map<String,String> createCert() throws Exception {
		final Map<String, String> csrRequest = createCsrRequest();
		final String privateKey = csrRequest.get(PRIVATE_KEY);
		final String publicKey = csrRequest.get(PUBLIC_KEY);
		return generateCertificate(privateKey,publicKey);
	}


	/**
	 * 构造 csr证书 请求文件
	 */
	private static Map<String,String> createCsrRequest() throws Exception {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

		// 创建密钥对
		KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
		gen.initialize(2048);
		KeyPair pair = gen.generateKeyPair();
		PrivateKey privateKey = pair.getPrivate();
		PublicKey publicKey = pair.getPublic();

		// 创建 CSR 对象
		X500Principal subject = new X500Principal("C=CName, ST=STName, L=LName, O=OName, OU=OUName, CN=CNName, EMAILADDRESS=" + EmailUtils.companyEmail);
		ContentSigner signGen = new JcaContentSignerBuilder("SHA1withRSA").build(privateKey);
		PKCS10CertificationRequestBuilder builder = new JcaPKCS10CertificationRequestBuilder(subject, publicKey);
		// 添加 SAN 扩展
		ExtensionsGenerator extensionsGenerator = new ExtensionsGenerator();
		GeneralNames generalNames = new GeneralNames(new GeneralName[]{new GeneralName(GeneralName.rfc822Name, "ip=6.6.6.6"), new GeneralName(GeneralName.rfc822Name, "email=" + EmailUtils.companyEmail)});
		extensionsGenerator.addExtension(Extension.subjectAlternativeName, false, generalNames);
		builder.addAttribute(PKCSObjectIdentifiers.pkcs_9_at_extensionRequest, extensionsGenerator.generate());
		// build csr
		PKCS10CertificationRequest csr = builder.build(signGen);

		// 输出 PEM 格式的 CSR
		// 输出 PEM 格式的 CSR
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		OutputStreamWriter output = new OutputStreamWriter(outputStream);
		JcaPEMWriter pem = new JcaPEMWriter(output);
		pem.writeObject(csr);
		pem.close();

		final byte[] publicKeyEncoded = outputStream.toByteArray();
		Map<String,String> res = new HashMap<>();

		String privateKeyStr = "-----BEGIN PRIVATE KEY-----\n" + Base64.encodeBase64String(privateKey.getEncoded()) + "\n-----END PRIVATE KEY-----\n";
		res.put("privateKey",privateKeyStr);
		res.put("publicKey", new String(publicKeyEncoded, StandardCharsets.UTF_8));
		return res;
	}

	private static Map<String, String> generateCertificate(String privateKey, String pem) throws IOException, CertificateException, NoSuchAlgorithmException, OperatorCreationException {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		// 解析 PEM 格式的 CSR
		PKCS10CertificationRequest csr = null;
		ByteArrayInputStream pemStream = new ByteArrayInputStream(pem.getBytes(Charsets.UTF_8));
		Reader pemReader = new BufferedReader(new InputStreamReader(pemStream));
		PEMParser pemParser = new PEMParser(pemReader);

		Object parsedObj = pemParser.readObject();
		if (parsedObj instanceof PKCS10CertificationRequest) {
			csr = (PKCS10CertificationRequest) parsedObj;
			final String parser = parser(csr);
			Map<String,String> res = new HashMap<>();
			res.put(PRIVATE_KEY, privateKey);
			res.put(PUBLIC_KEY, parser);
			return res;
		}
		return Collections.emptyMap();
	}

	// 提供一个根证书
	private final static String rootCert = "-----BEGIN CERTIFICATE-----\n" +
			"MIICnTCCAkOgAwIBAgIURXNvFrxLolzNI91BLv7SnvXhALcwCgYIKoZIzj0EAwIw\n" +
			"PzELMAkGA1UEBhMCQ04xDTALBgNVBAoTBG9yZzAxDzANBgNVBAsTBkZhYnJpYzEQ\n" +
			"MA4GA1UEAxMHY2Etb3JnMDAeFw0xOTA4MTkwMzA1MDBaFw0yOTA4MTYwMzEwMDBa\n" +
			"MG8xCzAJBgNVBAYTAlVTMRcwFQYDVQQIEw5Ob3J0aCBDYXJvbGluYTEUMBIGA1UE\n" +
			"ChMLSHlwZXJsZWRnZXIxHDANBgNVBAsTBmNsaWVudDALBgNVBAsTBG9yZzAxEzAR\n" +
			"BgNVBAMTCmFkbWluLW9yZzAwWTATBgcqhkjOPQIBBggqhkjOPQMBBwNCAAQwfsnX\n" +
			"kch9BnR2XGkfkfNqx7JM82uO53IaYNtFoGIPk8Xv914YSeqrEZiyeM186GFpJgmb\n" +
			"4Q5K4/DQgGHuJ117o4HsMIHpMA4GA1UdDwEB/wQEAwIHgDAMBgNVHRMBAf8EAjAA\n" +
			"MB0GA1UdDgQWBBRAHdIdDuPkXbZy7oDKWeNqaFTOHjAfBgNVHSMEGDAWgBT1jqJi\n" +
			"aCv/JVNoBwriqB/8jvnCAzAXBgNVHREEEDAOggxkZDYzNGVlMGM5YjcwcAYIKgME\n" +
			"BQYHCAEEZHsiYXR0cnMiOnsiYWRtaW4iOiJ0cnVlIiwiaGYuQWZmaWxpYXRpb24i\n" +
			"OiJvcmcwIiwiaGYuRW5yb2xsbWVudElEIjoiYWRtaW4tb3JnMCIsImhmLlR5cGUi\n" +
			"OiJjbGllbnQifX0wCgYIKoZIzj0EAwIDSAAwRQIhANvB2qVpd/pb+aWiDn1OattS\n" +
			"6StgFuaNikMpcMDlUpDtAiBNAYtLos8zV0pdjLk4sx0uzPLydLVZoHz8WRNZb6CX\n" +
			"uw==\n" +
			"-----END CERTIFICATE-----";

	private static String parser(PKCS10CertificationRequest pkcs10CertificationRequest) throws NoSuchAlgorithmException, CertificateException, IOException, OperatorCreationException {
		KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
		gen.initialize(2048);
		KeyPair rootPair = gen.generateKeyPair();
		// 私钥用来前面
		PrivateKey issuePriveteKey = rootPair.getPrivate();

		final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(rootCert.getBytes());

		CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
		X509Certificate rootCert = (X509Certificate)certificateFactory.generateCertificate(byteArrayInputStream);// 利用公钥创建根证书，来签发用户证书


		X509v3CertificateBuilder certificateBuilder = new X509v3CertificateBuilder(
				new X500Name(rootCert.getSubjectDN().getName()),
				BigInteger.valueOf(666666666L),
				new Date(),
				new Date(System.currentTimeMillis() + 1000 * 86400 * 365L),
				pkcs10CertificationRequest.getSubject(),
				pkcs10CertificationRequest.getSubjectPublicKeyInfo()
		);

		// 读取扩展信息
		Extensions extensions = null;
		for (Attribute attr : pkcs10CertificationRequest.getAttributes()) {
			if (PKCSObjectIdentifiers.pkcs_9_at_extensionRequest.equals(attr.getAttrType())) {
				extensions = Extensions.getInstance(attr.getAttributeValues()[0]);
				break;
			}
		}
		if (extensions != null) {
			// 添加 SAN 扩展
			certificateBuilder.addExtension(extensions.getExtension(Extension.subjectAlternativeName));
		}

		//添加crl扩展
		GeneralName[] names = new GeneralName[1];
		names[0] = new GeneralName(GeneralName.uniformResourceIdentifier, "http://www.ca.com/crl");
		GeneralNames gns = new GeneralNames(names);
		DistributionPointName pointName = new DistributionPointName(gns);
		GeneralNames crlIssuer = new GeneralNames(new GeneralName(new X500Name(rootCert.getSubjectDN().getName())));
		DistributionPoint[] points = new DistributionPoint[1];
		points[0] = new DistributionPoint(pointName, null, crlIssuer);
		certificateBuilder.addExtension(Extension.cRLDistributionPoints, false, new CRLDistPoint(points));

		//添加aia扩展
		AccessDescription[] accessDescriptions = new AccessDescription[2];
		accessDescriptions[0] = new AccessDescription(AccessDescription.id_ad_caIssuers, new GeneralName(GeneralName.uniformResourceIdentifier, "http://www.ca.com/root.crt"));
		accessDescriptions[1] = new AccessDescription(AccessDescription.id_ad_ocsp, new GeneralName(GeneralName.uniformResourceIdentifier, "http://ocsp.com/"));
		certificateBuilder.addExtension(Extension.authorityInfoAccess, false, new AuthorityInformationAccess(accessDescriptions));

		ContentSigner signer = new JcaContentSignerBuilder("SHA256withRSA")
				.setProvider(Security.getProvider("BC")).build(issuePriveteKey);
		X509CertificateHolder holder = certificateBuilder.build(signer);
		X509Certificate cert = new JcaX509CertificateConverter()
				.setProvider(Security.getProvider( "BC")).getCertificate(holder);


		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		JcaPEMWriter pem = new JcaPEMWriter(new OutputStreamWriter(outputStream));
		pem.writeObject(cert);
		pem.close();

		return new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
	}


	public static void main(String[] args) throws Exception {
		Map<String, String> keyMap = RSA2Utils.createKeys();
		Map<String, String> cert = RSA2Utils.createCert();
		String publicKey = cert.get(PUBLIC_KEY);
		String privateKey = cert.get(PRIVATE_KEY);
		System.out.println("公钥:" + publicKey);
		System.out.println("私钥:" + privateKey);
		System.out.println("公钥加密——私钥解密");
		String str = "fasfadsfs";
		System.out.println("明文:" + str);
		System.out.println("明文大小:" + str.getBytes().length);
		String encodedData = RSA2Utils.publicEncrypt(str, RSA2Utils.getPublicKey(publicKey));
		System.out.println("密文：" + encodedData);
		String decodedData = RSA2Utils.privateDecrypt(encodedData, RSA2Utils.getPrivateKey(privateKey));
		System.out.println("解密后文字:" + decodedData);
	}
	 
}
 
