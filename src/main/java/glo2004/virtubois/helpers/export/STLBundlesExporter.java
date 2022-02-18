package glo2004.virtubois.helpers.export;

import glo2004.virtubois.domain.dtos.BundleDto;
import glo2004.virtubois.helpers.STLGenerator;
import glo2004.virtubois.helpers.file.saver.FileSaver;

import java.util.List;

public class STLBundlesExporter implements BundlesExporter {

    private final STLGenerator stlGenerator;
    private final FileSaver fileSaver;

    public STLBundlesExporter(STLGenerator stlGenerator, FileSaver fileSaver) {
        this.stlGenerator = stlGenerator;
        this.fileSaver = fileSaver;
    }

    @Override
    public void exportBundles(List<BundleDto> bundleDtos) {
        String stlContent = stlGenerator.generateSTL(bundleDtos);
        fileSaver.save(stlContent, true);
    }
}
