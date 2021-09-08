import static com.atlassian.jira.workflow.TransitionOptions.Builder;
// ....
if (skipConditions)
{
    final TransitionOptions trasitionOptions = new Builder().skipPermissions().skipValidators().setAutomaticTransition().skipConditions().build();
    transitionValidationResult = issueService.validateTransition(user,
            mutableIssue.getId(), getTransitionId(actionId), params, trasitionOptions);
}
