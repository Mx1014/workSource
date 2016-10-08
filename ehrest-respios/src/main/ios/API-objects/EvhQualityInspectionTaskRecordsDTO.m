//
// EvhQualityInspectionTaskRecordsDTO.m
//
#import "EvhQualityInspectionTaskRecordsDTO.h"
#import "EvhQualityInspectionTaskAttachmentDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQualityInspectionTaskRecordsDTO
//

@implementation EvhQualityInspectionTaskRecordsDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhQualityInspectionTaskRecordsDTO* obj = [EvhQualityInspectionTaskRecordsDTO new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _attachments = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.taskId)
        [jsonObject setObject: self.taskId forKey: @"taskId"];
    if(self.operatorId)
        [jsonObject setObject: self.operatorId forKey: @"operatorId"];
    if(self.targetId)
        [jsonObject setObject: self.targetId forKey: @"targetId"];
    if(self.targetName)
        [jsonObject setObject: self.targetName forKey: @"targetName"];
    if(self.processType)
        [jsonObject setObject: self.processType forKey: @"processType"];
    if(self.processEndTime)
        [jsonObject setObject: self.processEndTime forKey: @"processEndTime"];
    if(self.processResult)
        [jsonObject setObject: self.processResult forKey: @"processResult"];
    if(self.processMessage)
        [jsonObject setObject: self.processMessage forKey: @"processMessage"];
    if(self.behaviorTime)
        [jsonObject setObject: self.behaviorTime forKey: @"behaviorTime"];
    if(self.attachments) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhQualityInspectionTaskAttachmentDTO* item in self.attachments) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"attachments"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.taskId = [jsonObject objectForKey: @"taskId"];
        if(self.taskId && [self.taskId isEqual:[NSNull null]])
            self.taskId = nil;

        self.operatorId = [jsonObject objectForKey: @"operatorId"];
        if(self.operatorId && [self.operatorId isEqual:[NSNull null]])
            self.operatorId = nil;

        self.targetId = [jsonObject objectForKey: @"targetId"];
        if(self.targetId && [self.targetId isEqual:[NSNull null]])
            self.targetId = nil;

        self.targetName = [jsonObject objectForKey: @"targetName"];
        if(self.targetName && [self.targetName isEqual:[NSNull null]])
            self.targetName = nil;

        self.processType = [jsonObject objectForKey: @"processType"];
        if(self.processType && [self.processType isEqual:[NSNull null]])
            self.processType = nil;

        self.processEndTime = [jsonObject objectForKey: @"processEndTime"];
        if(self.processEndTime && [self.processEndTime isEqual:[NSNull null]])
            self.processEndTime = nil;

        self.processResult = [jsonObject objectForKey: @"processResult"];
        if(self.processResult && [self.processResult isEqual:[NSNull null]])
            self.processResult = nil;

        self.processMessage = [jsonObject objectForKey: @"processMessage"];
        if(self.processMessage && [self.processMessage isEqual:[NSNull null]])
            self.processMessage = nil;

        self.behaviorTime = [jsonObject objectForKey: @"behaviorTime"];
        if(self.behaviorTime && [self.behaviorTime isEqual:[NSNull null]])
            self.behaviorTime = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"attachments"];
            for(id itemJson in jsonArray) {
                EvhQualityInspectionTaskAttachmentDTO* item = [EvhQualityInspectionTaskAttachmentDTO new];
                
                [item fromJson: itemJson];
                [self.attachments addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
