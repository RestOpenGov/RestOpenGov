rm -fr ../../RestOpenGov.openshift/playdemo/*

cp -r * ../../RestOpenGov.openshift/playdemo

pushd ../../RestOpenGov.openshift/playdemo

./.openshift_deploy 

popd