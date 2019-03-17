package helpers;

import domain.dtos.BundleDto;
import domain.entities.Bundle;

import java.util.ArrayList;
import java.util.List;

public class Converter {

    public static List<BundleDto> fromBundlesToBundleDtos(List<Bundle> bundles) {
        List<BundleDto> dtos = new ArrayList<>();
        for (Bundle bundle : bundles) {
            dtos.add(new BundleDto(bundle));
        }
        return dtos;
    }

    public static CenteredRectangle fromBundleToCenteredRectangle(Bundle bundle) {
        return new CenteredRectangle(
                bundle.getPosition(),
                bundle.getWidth(),
                bundle.getLength(),
                bundle.getAngle()
        );
    }
}