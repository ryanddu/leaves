

package com.github.ryanddu.utils;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

/**
 * http客户端工具类
 * @author: ryan
 * @date: 2023/4/12 14:04
 **/
public class OkHttpUtils {
    private static final Logger log = LoggerFactory.getLogger(OkHttpUtils.class);
    private static final OkHttpClient DEFAULT_CLIENT;
    public static final int READ_TIMEOUT = 15;
    public static final int CONNECT_TIMEOUT = 3;
    public static final int WRITE_TIMEOUT = 15;
    public static final int POOL_MAX_IDLE = 50;
    public static final int POOL_KEEP_ALIVE = 180;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType MULTIPART = MediaType.parse("multipart/form-data");

    public OkHttpUtils() {
    }

    private static X509TrustManager x509TrustManager() {
        return new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
    }

    private static SSLSocketFactory sslSocketFactory() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init((KeyManager[])null, new TrustManager[]{x509TrustManager()}, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException var1) {
        } catch (KeyManagementException var2) {
        }

        return null;
    }

    public static HttpClient client() {
        return new HttpClient(DEFAULT_CLIENT);
    }

    public static HttpClient client(OkHttpClient okHttpClient) {
        return new HttpClient(okHttpClient);
    }

    static {
        ConnectionPool connectionPool = new ConnectionPool(50, 180L, TimeUnit.SECONDS);
        DEFAULT_CLIENT = (new OkHttpClient.Builder()).sslSocketFactory(sslSocketFactory(), x509TrustManager()).retryOnConnectionFailure(true).connectionPool(connectionPool).connectTimeout(3L, TimeUnit.SECONDS).readTimeout(15L, TimeUnit.SECONDS).writeTimeout(15L, TimeUnit.SECONDS).connectionSpecs(Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.CLEARTEXT)).hostnameVerifier((s, sslSession) -> {
            return true;
        }).build();
    }

    public static class HttpClient {
        private OkHttpClient okHttpClient;

        public HttpClient(OkHttpClient okHttpClient) {
            this.okHttpClient = okHttpClient;
        }

        public CallWrapper get(String url) {
            return this.get(url, (Map)null, (Map)null);
        }

        public CallWrapper get(String url, Map<String, Object> params) {
            return this.get(url, params, (Map)null);
        }

        public CallWrapper get(String url, Map<String, Object> params, Map<String, Object> headers) {
            OkHttpUtils.log.debug("GET [{}], params:[{}], headers:[{}]", new Object[]{url, params, headers});
            Request.Builder builder = new Request.Builder();
            HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
            if (params != null) {
                params.forEach((k, v) -> {
                    if (v != null) {
                        urlBuilder.addEncodedQueryParameter(k, v.toString());
                    }
                });
            }

            builder.url(urlBuilder.build());
            this.buildHeader(headers, builder);
            Request request = builder.build();
            return this.doRequest(request);
        }

        private void buildHeader(Map<String, Object> headers, Request.Builder builder) {
            if (headers != null) {
                headers.forEach((k, v) -> {
                    if (v != null) {
                        builder.addHeader(k, v.toString());
                    }
                });
            }

        }

        public CallWrapper post(String url, Map<String, Object> bodyParams) {
            return this.post(url, (Map)bodyParams, (Map)null);
        }

        public CallWrapper post(String url, Map<String, Object> bodyParams, Map<String, Object> headers) {
            OkHttpUtils.log.debug("POST [{}], params:[{}], headers:[{}]", url, bodyParams);
            RequestBody body = this.setRequestBody(bodyParams);
            Request.Builder builder = new Request.Builder();
            this.buildHeader(headers, builder);
            Request request = builder.post(body).url(url).build();
            return this.doRequest(request);
        }

        public CallWrapper postFile(String url, InputStream in, String fileName, Map<String, Object> bodyParams) {
            return this.postFile(url, in, fileName, bodyParams, (Map)null);
        }

        public CallWrapper postFile(String url, InputStream in, String fileName, Map<String, Object> bodyParams, Map<String, Object> headers) {
            OkHttpUtils.log.debug("POST_FILE [{}], fileName:[{}], params:[{}], headers:[{}]", new Object[]{url, fileName, bodyParams, headers});
            RequestBody body = null;

            try {
                body = this.setFileRequestBody(in, fileName, bodyParams);
            } catch (IOException var9) {
                OkHttpUtils.log.error("body构造失败", var9);
                return null;
            }

            Request.Builder builder = new Request.Builder();
            this.buildHeader(headers, builder);
            Request request = builder.post(body).url(url).build();
            return this.doRequest(request);
        }

        public CallWrapper postJson(String url, String json) {
            return this.postJson(url, json, (Map)null);
        }

        public CallWrapper postJson(String url, String json, Map<String, Object> headers) {
            return this.post(url, json, OkHttpUtils.JSON, headers);
        }

        public CallWrapper post(String url, String body, MediaType mediaType) {
            return this.post(url, body, mediaType, (Map)null);
        }

        public CallWrapper post(String url, String body, MediaType mediaType, Map<String, Object> headers) {
            OkHttpUtils.log.debug("POST_[{}] [{}], body:[{}], headers:[{}]", new Object[]{mediaType, url, body, headers});
            RequestBody requestBody = RequestBody.create(mediaType, body);
            Request.Builder builder = new Request.Builder();
            this.buildHeader(headers, builder);
            Request request = builder.url(url).post(requestBody).build();
            return this.doRequest(request);
        }

        private RequestBody setRequestBody(Map<String, Object> bodyParams) {
            FormBody.Builder formEncodingBuilder = new FormBody.Builder();
            if (bodyParams != null) {
                bodyParams.forEach((k, v) -> {
                    if (v != null) {
                        formEncodingBuilder.add(k, v.toString());
                    }
                });
            }

            RequestBody body = formEncodingBuilder.build();
            return body;
        }

        private RequestBody setFileRequestBody(InputStream in, String fileName, Map<String, Object> bodyParams) throws IOException {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            RequestBody fileBody;
            try {
                byte[] buff = new byte[2048];

                while(true) {
                    int rc;
                    if ((rc = in.read(buff)) <= 0) {
                        byte[] bytes = bos.toByteArray();
                        fileBody = RequestBody.create(OkHttpUtils.MULTIPART, bytes);
                        break;
                    }

                    bos.write(buff, 0, rc);
                }
            } catch (Throwable var10) {
                try {
                    bos.close();
                } catch (Throwable var9) {
                    var10.addSuppressed(var9);
                }

                throw var10;
            }

            bos.close();
            MultipartBody.Builder builder = (new MultipartBody.Builder()).setType(MultipartBody.FORM).addFormDataPart("file", fileName, fileBody);
            if (bodyParams != null) {
                bodyParams.forEach((k, v) -> {
                    if (v != null) {
                        builder.addFormDataPart(k, v.toString());
                    }
                });
            }

            RequestBody requestBody = builder.build();
            return requestBody;
        }

        private CallWrapper doRequest(Request request) {
            Call call = this.okHttpClient.newCall(request);
            return new CallWrapper(call);
        }
    }

    @FunctionalInterface
    public interface Function<T, R> {
        R apply(T var1) throws IOException;

        default <V> Function<V, R> compose(Function<? super V, ? extends T> before) {
            Objects.requireNonNull(before);
            return (v) -> this.apply(before.apply(v));
        }

        default <V> Function<T, V> andThen(Function<? super R, ? extends V> after) {
            Objects.requireNonNull(after);
            return (t) -> after.apply(this.apply(t));
        }

        static <T> Function<T, T> identity() {
            return (t) -> t;
        }
    }

    public static class CallWrapper {
        private Call call;
        private boolean asyn = false;
        private Function<Response, ?> ok = (response) -> {
            String result = response.body().string();
            OkHttpUtils.log.debug("返回结果：{}", result);
            return result;
        };
        private BiConsumer<Call, IOException> fail = (callx, e) -> {
            OkHttpUtils.log.error("请求失败:[{}]", callx.request().url().encodedPath(), e);
        };

        public CallWrapper(Call call) {
            this.call = call;
        }

        public CallWrapper asyn(boolean asyn) {
            this.asyn = asyn;
            return this;
        }

        public CallWrapper ok(Function<Response, ?> ok) {
            this.ok = ok;
            return this;
        }

        public CallWrapper fail(BiConsumer<Call, IOException> fail) {
            this.fail = fail;
            return this;
        }

        public void execute() {
            final long now = System.currentTimeMillis();
            if (!this.asyn) {
                try {
                    Response response = this.call.execute();
                    OkHttpUtils.log.debug("请求耗时:{}ms", System.currentTimeMillis() - now);
                    int code = response.code();
                    if (code >= 200 && code < 300) {
                         this.ok.apply(response);
                    }

                    throw new IOException("http响应状态码错误:" + code);
                } catch (IOException var5) {
                    this.fail.accept(this.call, var5);
                }
            } else {
                this.call.enqueue(new Callback() {
                    public void onFailure(Call call, IOException e) {
                        CallWrapper.this.fail.accept(call, e);
                    }

                    public void onResponse(Call call, Response response) {
                        OkHttpUtils.log.debug("请求耗时:{}ms ==> {}", System.currentTimeMillis() - now);
                        int code = response.code();

                        try {
                            if (code < 200 || code >= 300) {
                                OkHttpUtils.log.error("http响应状态码错误:[{}],[{}]", code, response.body().string());
                                throw new IOException("http响应状态码错误:" + code);
                            }

                            CallWrapper.this.ok.apply(response);
                        } catch (IOException var5) {
                            CallWrapper.this.fail.accept(call, var5);
                        }

                    }
                });
            }
        }
    }
}
