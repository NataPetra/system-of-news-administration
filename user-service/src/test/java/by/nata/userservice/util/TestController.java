package by.nata.userservice.util;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping()
    public ResponseEntity<String> getResource() {
        return ResponseEntity.ok("This is a not protected resource!");
    }

    @GetMapping("/protected/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> getProtectedResource() {
        return ResponseEntity.ok("This is a protected resource!");
    }

    @GetMapping("/protected/subscriber")
    @PreAuthorize("hasRole('SUBSCRIBER')")
    public ResponseEntity<String> getSubscriberProtectedResource() {
        return ResponseEntity.ok("This is a protected resource!");
    }

    @GetMapping("/protected/journalist")
    @PreAuthorize("hasRole('JOURNALIST')")
    public ResponseEntity<String> getJournalistProtectedResource() {
        return ResponseEntity.ok("This is a protected resource!");
    }
}
