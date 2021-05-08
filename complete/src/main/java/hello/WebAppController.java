package hello;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.google.pubsub.v1.PubsubMessage;
import hello.PubSubApplication.PubsubOutboundGateway;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.cloud.gcp.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import org.springframework.cloud.gcp.pubsub.support.AcknowledgeablePubsubMessage;
import org.springframework.cloud.gcp.pubsub.support.BasicAcknowledgeablePubsubMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@EnableAutoConfiguration
public class WebAppController {

  @Autowired
  private PubSubTemplate pubSubTemplate;

  @GetMapping("/")
  public String ini() {
    return "hello";
  }

  @PostMapping("/publishMessage")
  public String publishMessage(Model model, @RequestParam("message") String message) {
    //	messagingGateway.sendToPubsub(message);
    pubSubTemplate.publish("topic_1", message);
    return "hello";
  }

  @GetMapping("/getMessage")
  public String getMessage(Model model) {
    List<AcknowledgeablePubsubMessage> messages = pubSubTemplate.pull("subscribe_1", 200, true);
    for (AcknowledgeablePubsubMessage me : messages) {
      PubsubMessage pme = me.getPubsubMessage();
      System.out
          .println("bao print###time: " + pme.getPublishTime() + ", bao print###value: " + pme.getData().toStringUtf8());
      model.addAttribute("msg", me.getPubsubMessage().getData().toStringUtf8());
    }
    return "hello";
  }


}
