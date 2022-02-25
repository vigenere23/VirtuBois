package glo2004.virtubois.domain.shared;

import glo2004.virtubois.domain.dtos.BundleDto;

import java.nio.file.Path;
import java.util.List;

public interface BundlesExporter {
    void exportBundles(Path path, List<BundleDto> bundleDtos);
}
