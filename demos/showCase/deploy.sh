rm -fr ../../../RestOpenGov.openshift/demos/showCase/*

# resolve symlinks
cp -rL * ../../../RestOpenGov.openshift/demos/showCase/

pushd ../../../RestOpenGov.openshift/demos/showCase

./.openshift_deploy

popd