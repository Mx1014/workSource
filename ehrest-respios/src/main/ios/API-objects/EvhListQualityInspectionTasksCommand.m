//
// EvhListQualityInspectionTasksCommand.m
//
#import "EvhListQualityInspectionTasksCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListQualityInspectionTasksCommand
//

@implementation EvhListQualityInspectionTasksCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListQualityInspectionTasksCommand* obj = [EvhListQualityInspectionTasksCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.ownerId)
        [jsonObject setObject: self.ownerId forKey: @"ownerId"];
    if(self.ownerType)
        [jsonObject setObject: self.ownerType forKey: @"ownerType"];
    if(self.taskType)
        [jsonObject setObject: self.taskType forKey: @"taskType"];
    if(self.executeFlag)
        [jsonObject setObject: self.executeFlag forKey: @"executeFlag"];
    if(self.isReview)
        [jsonObject setObject: self.isReview forKey: @"isReview"];
    if(self.startDate)
        [jsonObject setObject: self.startDate forKey: @"startDate"];
    if(self.endDate)
        [jsonObject setObject: self.endDate forKey: @"endDate"];
    if(self.groupId)
        [jsonObject setObject: self.groupId forKey: @"groupId"];
    if(self.executeStatus)
        [jsonObject setObject: self.executeStatus forKey: @"executeStatus"];
    if(self.reviewStatus)
        [jsonObject setObject: self.reviewStatus forKey: @"reviewStatus"];
    if(self.manualFlag)
        [jsonObject setObject: self.manualFlag forKey: @"manualFlag"];
    if(self.pageAnchor)
        [jsonObject setObject: self.pageAnchor forKey: @"pageAnchor"];
    if(self.pageSize)
        [jsonObject setObject: self.pageSize forKey: @"pageSize"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.ownerId = [jsonObject objectForKey: @"ownerId"];
        if(self.ownerId && [self.ownerId isEqual:[NSNull null]])
            self.ownerId = nil;

        self.ownerType = [jsonObject objectForKey: @"ownerType"];
        if(self.ownerType && [self.ownerType isEqual:[NSNull null]])
            self.ownerType = nil;

        self.taskType = [jsonObject objectForKey: @"taskType"];
        if(self.taskType && [self.taskType isEqual:[NSNull null]])
            self.taskType = nil;

        self.executeFlag = [jsonObject objectForKey: @"executeFlag"];
        if(self.executeFlag && [self.executeFlag isEqual:[NSNull null]])
            self.executeFlag = nil;

        self.isReview = [jsonObject objectForKey: @"isReview"];
        if(self.isReview && [self.isReview isEqual:[NSNull null]])
            self.isReview = nil;

        self.startDate = [jsonObject objectForKey: @"startDate"];
        if(self.startDate && [self.startDate isEqual:[NSNull null]])
            self.startDate = nil;

        self.endDate = [jsonObject objectForKey: @"endDate"];
        if(self.endDate && [self.endDate isEqual:[NSNull null]])
            self.endDate = nil;

        self.groupId = [jsonObject objectForKey: @"groupId"];
        if(self.groupId && [self.groupId isEqual:[NSNull null]])
            self.groupId = nil;

        self.executeStatus = [jsonObject objectForKey: @"executeStatus"];
        if(self.executeStatus && [self.executeStatus isEqual:[NSNull null]])
            self.executeStatus = nil;

        self.reviewStatus = [jsonObject objectForKey: @"reviewStatus"];
        if(self.reviewStatus && [self.reviewStatus isEqual:[NSNull null]])
            self.reviewStatus = nil;

        self.manualFlag = [jsonObject objectForKey: @"manualFlag"];
        if(self.manualFlag && [self.manualFlag isEqual:[NSNull null]])
            self.manualFlag = nil;

        self.pageAnchor = [jsonObject objectForKey: @"pageAnchor"];
        if(self.pageAnchor && [self.pageAnchor isEqual:[NSNull null]])
            self.pageAnchor = nil;

        self.pageSize = [jsonObject objectForKey: @"pageSize"];
        if(self.pageSize && [self.pageSize isEqual:[NSNull null]])
            self.pageSize = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
