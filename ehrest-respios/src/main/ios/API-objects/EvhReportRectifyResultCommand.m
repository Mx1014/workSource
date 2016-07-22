//
// EvhReportRectifyResultCommand.m
//
#import "EvhReportRectifyResultCommand.h"
#import "EvhForumAttachmentDescriptor.h"

///////////////////////////////////////////////////////////////////////////////
// EvhReportRectifyResultCommand
//

@implementation EvhReportRectifyResultCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhReportRectifyResultCommand* obj = [EvhReportRectifyResultCommand new];
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
    if(self.taskId)
        [jsonObject setObject: self.taskId forKey: @"taskId"];
    if(self.rectifyResult)
        [jsonObject setObject: self.rectifyResult forKey: @"rectifyResult"];
    if(self.endTime)
        [jsonObject setObject: self.endTime forKey: @"endTime"];
    if(self.operatorType)
        [jsonObject setObject: self.operatorType forKey: @"operatorType"];
    if(self.operatorId)
        [jsonObject setObject: self.operatorId forKey: @"operatorId"];
    if(self.attachments) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhForumAttachmentDescriptor* item in self.attachments) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"attachments"];
    }
    if(self.message)
        [jsonObject setObject: self.message forKey: @"message"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.taskId = [jsonObject objectForKey: @"taskId"];
        if(self.taskId && [self.taskId isEqual:[NSNull null]])
            self.taskId = nil;

        self.rectifyResult = [jsonObject objectForKey: @"rectifyResult"];
        if(self.rectifyResult && [self.rectifyResult isEqual:[NSNull null]])
            self.rectifyResult = nil;

        self.endTime = [jsonObject objectForKey: @"endTime"];
        if(self.endTime && [self.endTime isEqual:[NSNull null]])
            self.endTime = nil;

        self.operatorType = [jsonObject objectForKey: @"operatorType"];
        if(self.operatorType && [self.operatorType isEqual:[NSNull null]])
            self.operatorType = nil;

        self.operatorId = [jsonObject objectForKey: @"operatorId"];
        if(self.operatorId && [self.operatorId isEqual:[NSNull null]])
            self.operatorId = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"attachments"];
            for(id itemJson in jsonArray) {
                EvhForumAttachmentDescriptor* item = [EvhForumAttachmentDescriptor new];
                
                [item fromJson: itemJson];
                [self.attachments addObject: item];
            }
        }
        self.message = [jsonObject objectForKey: @"message"];
        if(self.message && [self.message isEqual:[NSNull null]])
            self.message = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
