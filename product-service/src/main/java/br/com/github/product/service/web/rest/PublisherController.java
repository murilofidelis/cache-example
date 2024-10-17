package br.com.github.product.service.web.rest;


import br.com.github.product.service.dto.MessageRequest;
import br.com.github.product.service.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pub-sub")
public class PublisherController {
    private final PublisherService publish;

    @Autowired
    public PublisherController(PublisherService service) {
        this.publish = service;
    }

    @PostMapping("publish")
    public ResponseEntity<Void> publish(@RequestBody MessageRequest messageRequest) {

        publish.publish(messageRequest);

        return ResponseEntity.ok().build();
    }
}
