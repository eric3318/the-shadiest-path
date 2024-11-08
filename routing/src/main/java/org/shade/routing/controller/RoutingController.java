package org.shade.routing.controller;


import lombok.RequiredArgsConstructor;
import org.shade.routing.dto.RouteRequest;
import org.shade.routing.service.RoutingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RoutingController {

  private final RoutingService routingService;

  @GetMapping("/edges")
  public ResponseEntity<?> getEdges(@RequestParam double minLon, @RequestParam double maxLon,
      @RequestParam double minLat, @RequestParam double maxLat) {
    return new ResponseEntity<>(routingService.getEdges(minLon, maxLon, minLat, maxLat),
        HttpStatus.OK);
  }

  @PostMapping("/route")
  public ResponseEntity<?> route(@RequestBody RouteRequest routeRequest) {
    return new ResponseEntity<>(routingService.getRoute(routeRequest), HttpStatus.OK);
  }
}
