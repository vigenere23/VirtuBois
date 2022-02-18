package glo2004.virtubois.helpers.export;

import glo2004.virtubois.domain.dtos.BundleDto;

import java.util.List;

public interface BundlesExporter {
    void exportBundles(List<BundleDto> bundleDtos);
}
