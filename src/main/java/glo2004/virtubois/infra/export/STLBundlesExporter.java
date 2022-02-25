package glo2004.virtubois.infra.export;

import glo2004.virtubois.domain.dtos.BundleDto;
import glo2004.virtubois.domain.shared.BundlesExporter;
import glo2004.virtubois.domain.shared.FileWriter;
import glo2004.virtubois.helpers.STLGenerator;

import java.nio.file.Path;
import java.util.List;

public class STLBundlesExporter implements BundlesExporter {

    private final STLGenerator stlGenerator;
    private final FileWriter fileWriter;

    public STLBundlesExporter(STLGenerator stlGenerator, FileWriter fileWriter) {
        this.stlGenerator = stlGenerator;
        this.fileWriter = fileWriter;
    }

    @Override
    public void exportBundles(Path path, List<BundleDto> bundleDtos) {
        String stlContent = stlGenerator.generateSTL(bundleDtos);
        fileWriter.write(path, stlContent);
    }
}
