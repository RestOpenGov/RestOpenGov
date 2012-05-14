rm -fr ../../RestOpenGov.openshift/PlayBafici/*

cp -r * ../../RestOpenGov.openshift/PlayBafici

pushd ../../RestOpenGov.openshift/PlayBafici

./.openshift_deploy 

popd