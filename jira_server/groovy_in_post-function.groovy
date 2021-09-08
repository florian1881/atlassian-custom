//添加评论
issueInputParameters.setComment('该Bug在创建时已经指定了修复的Sprint、涉及的模块、子任务负责人，因此系统跳过“正在分配Sprint”阶段，自动为各负责人创建子任务，并直接进入“正在协作处理”状态。')

log.debug "key: ${issue.key}"
