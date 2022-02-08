package virtubois.helpers;

import virtubois.domain.dtos.BundleDto;
import virtubois.domain.entities.Bundle;
import virtubois.presentation.presenters.BundlePresenter;

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

    public static List<BundlePresenter> fromBundleDtosToBundlePresenters(List<BundleDto> bundleDtos) {
        List<BundlePresenter> presenters = new ArrayList<>();
        for (BundleDto bundleDto : bundleDtos) {
            presenters.add(new BundlePresenter(bundleDto));
        }
        return presenters;
    }
}