import java.util.UUID;

import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.ConfigMapBuilder;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.utils.HttpClientUtils;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);


    public static void main(String[] argv) {

        log.info("Started");

        final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        final Config config = new ConfigBuilder().build();
        OkHttpClient httpClient = HttpClientUtils.createHttpClient(config);
        httpClient = httpClient.newBuilder()
                               .addInterceptor(interceptor)
                               .build();
        final DefaultKubernetesClient client  = new DefaultKubernetesClient(httpClient, config);

        final ConfigMap cm = new ConfigMapBuilder()
                .withNewMetadata().withName("kwtest-" + UUID.randomUUID().toString())
                .endMetadata()
                .build();
        final ConfigMap created = client.configMaps().create(cm);
        log.info("Created {}", created.getMetadata().getName());

        client.configMaps().delete(created);
        log.info("Deleted {}", created.getMetadata().getName());


    }
}
