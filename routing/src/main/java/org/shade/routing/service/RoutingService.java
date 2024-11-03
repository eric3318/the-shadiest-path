package org.shade.routing.service;


import com.graphhopper.GraphHopper;
import com.graphhopper.storage.Graph;
import com.graphhopper.storage.index.LocationIndex;
import com.graphhopper.storage.index.LocationIndex.Visitor;
import com.graphhopper.util.EdgeIteratorState;
import com.graphhopper.util.FetchMode;
import com.graphhopper.util.PointList;
import com.graphhopper.util.shapes.BBox;
import com.graphhopper.util.shapes.GHPoint;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.shade.routing.dto.BBoxDto;
import org.shade.routing.dto.BoundingBoxDto;
import org.shade.routing.utils.MapUtil;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoutingService {

  private final GraphHopper hopper;

  public void getRoute() {
  }

  public List<BBoxDto> getBoundingBoxes(double minLon, double maxLon, double minLat,
      double maxLat) {
    return MapUtil.getBBoxCells(-122.98701, -122.96027, 49.24033, 49.25313).stream()
        .map((bBox ->
            new BBoxDto(bBox.minLon, bBox.maxLon, bBox.minLat, bBox.maxLat))
        ).toList();
  }

  public List<BoundingBoxDto> getEdges(double minLon, double maxLon, double minLat,
      double maxLat) {
    LocationIndex locationIndex = hopper.getLocationIndex();
    List<BBox> bBoxList = MapUtil.getBBoxCells(-122.98701, -122.96027, 49.24033, 49.25313);
    Graph graph = hopper.getBaseGraph();
    List<BoundingBoxDto> result = new ArrayList<>();
    List<List<Double>> cell = new ArrayList<>();
    Visitor v = i -> {
      EdgeIteratorState iteratorState = graph.getEdgeIteratorState(i, Integer.MIN_VALUE);
      PointList geometry = iteratorState.fetchWayGeometry(FetchMode.ALL);
      List<Double> edge = new ArrayList<>();

      for (int idx = 0; idx < geometry.size(); idx++) {
        GHPoint ghPoint = geometry.get(idx);
        edge.add(ghPoint.getLon());
        edge.add(ghPoint.getLat());
      }
      cell.add(edge);
    };
    for (BBox bBox : bBoxList) {
      locationIndex.query(bBox, v);
      BBoxDto bBoxDto = new BBoxDto(bBox.minLon, bBox.maxLon, bBox.minLat, bBox.maxLat);
      BoundingBoxDto boundingBoxDto = new BoundingBoxDto(bBoxDto, List.copyOf(cell));
      result.add(boundingBoxDto);
      cell.clear();
    }
    return result;
  }
}
