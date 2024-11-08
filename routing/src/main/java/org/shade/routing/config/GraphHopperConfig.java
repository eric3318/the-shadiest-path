package org.shade.routing.config;

import com.graphhopper.GraphHopper;
import com.graphhopper.config.Profile;
import com.graphhopper.shaded.ShadedGraphHopper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GraphHopperConfig {

  @Bean
  public GraphHopper graphHopper() {
    GraphHopper hopper = new ShadedGraphHopper();
    hopper.setOSMFile("routing/src/main/resources/static/british-columbia-latest.osm.pbf");
    hopper.setGraphHopperLocation("routing/target/graph-cache");
    hopper.setEncodedValuesString("car_access, car_average_speed");
    hopper.setProfiles(
        new Profile("shaded"));
    hopper.importOrLoad();
    return hopper;
  }
}