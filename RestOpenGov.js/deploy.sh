rm -fr ../../RestOpenGov.openshift/openbafici/*

cp -r * ../../RestOpenGov.openshift/openbafici

pushd ../../RestOpenGov.openshift/openbafici

./.openshift_deploy 

popd