//Will add the BitbucketAgedRefsTrait to multibranch pipelines that don't already have one set. Assumes you are only using Bitbucket Branch Sources on the controller, not any GitHub.

import org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject;
import org.jenkinsci.plugins.scm_filter.BitbucketAgedRefsTrait;

dryRun = true

Jenkins.instance.getAllItems(WorkflowMultiBranchProject.class).each {
  println it.fullName
  sources = it.getSources()
  for (source in sources){
    scmSource = source.getSource()
    traits = scmSource.getTraits()
    if (!containsBitbucketAgedRefsTrait(traits)) {
      println '  No age limit currently set, adding the default.'
      newTraits = new ArrayList()
      for (t in traits) {newTraits.add(t)}
      newTraits.add(new BitbucketAgedRefsTrait('60'))
      if(!dryRun){
        scmSource.setTraits(newTraits)
      }
    } else {
      println '  Branch age limit is already set.'
    }
  }
  if(!dryRun){
    it.save()
  }
};
return
  
def boolean containsBitbucketAgedRefsTrait(List list) {
    for ( item in list) {
        if (item instanceof org.jenkinsci.plugins.scm_filter.BitbucketAgedRefsTrait) {
            return true;
        }
    }
    return false;
}
