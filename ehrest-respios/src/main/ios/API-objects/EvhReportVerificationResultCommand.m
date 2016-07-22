//
// EvhReportVerificationResultCommand.m
//
#import "EvhReportVerificationResultCommand.h"
#import "EvhForumAttachmentDescriptor.h"

///////////////////////////////////////////////////////////////////////////////
// EvhReportVerificationResultCommand
//

@implementation EvhReportVerificationResultCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhReportVerificationResultCommand* obj = [EvhReportVerificationResultCommand new];
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
    if(self.verificationResult)
        [jsonObject setObject: self.verificationResult forKey: @"verificationResult"];
    if(self.attachments) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhForumAttachmentDescriptor* item in self.attachments) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"attachments"];
    }
    if(self.endTime)
        [jsonObject setObject: self.endTime forKey: @"endTime"];
    if(self.operatorType)
        [jsonObject setObject: self.operatorType forKey: @"operatorType"];
    if(self.operatorId)
        [jsonObject setObject: self.operatorId forKey: @"operatorId"];
    if(self.message)
        [jsonObject setObject: self.message forKey: @"message"];
    if(self.categoryId)
        [jsonObject setObject: self.categoryId forKey: @"categoryId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.taskId = [jsonObject objectForKey: @"taskId"];
        if(self.taskId && [self.taskId isEqual:[NSNull null]])
            self.taskId = nil;

        self.verificationResult = [jsonObject objectForKey: @"verificationResult"];
        if(self.verificationResult && [self.verificationResult isEqual:[NSNull null]])
            self.verificationResult = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"attachments"];
            for(id itemJson in jsonArray) {
                EvhForumAttachmentDescriptor* item = [EvhForumAttachmentDescriptor new];
                
                [item fromJson: itemJson];
                [self.attachments addObject: item];
            }
        }
        self.endTime = [jsonObject objectForKey: @"endTime"];
        if(self.endTime && [self.endTime isEqual:[NSNull null]])
            self.endTime = nil;

        self.operatorType = [jsonObject objectForKey: @"operatorType"];
        if(self.operatorType && [self.operatorType isEqual:[NSNull null]])
            self.operatorType = nil;

        self.operatorId = [jsonObject objectForKey: @"operatorId"];
        if(self.operatorId && [self.operatorId isEqual:[NSNull null]])
            self.operatorId = nil;

        self.message = [jsonObject objectForKey: @"message"];
        if(self.message && [self.message isEqual:[NSNull null]])
            self.message = nil;

        self.categoryId = [jsonObject objectForKey: @"categoryId"];
        if(self.categoryId && [self.categoryId isEqual:[NSNull null]])
            self.categoryId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
