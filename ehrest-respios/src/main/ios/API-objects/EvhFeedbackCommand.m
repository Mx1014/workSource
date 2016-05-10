//
// EvhFeedbackCommand.m
//
#import "EvhFeedbackCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFeedbackCommand
//

@implementation EvhFeedbackCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhFeedbackCommand* obj = [EvhFeedbackCommand new];
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
    if(self.feedbackType)
        [jsonObject setObject: self.feedbackType forKey: @"feedbackType"];
    if(self.targetType)
        [jsonObject setObject: self.targetType forKey: @"targetType"];
    if(self.targetId)
        [jsonObject setObject: self.targetId forKey: @"targetId"];
    if(self.contentCategory)
        [jsonObject setObject: self.contentCategory forKey: @"contentCategory"];
    if(self.contact)
        [jsonObject setObject: self.contact forKey: @"contact"];
    if(self.subject)
        [jsonObject setObject: self.subject forKey: @"subject"];
    if(self.content)
        [jsonObject setObject: self.content forKey: @"content"];
    if(self.proofResourceUri)
        [jsonObject setObject: self.proofResourceUri forKey: @"proofResourceUri"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.feedbackType = [jsonObject objectForKey: @"feedbackType"];
        if(self.feedbackType && [self.feedbackType isEqual:[NSNull null]])
            self.feedbackType = nil;

        self.targetType = [jsonObject objectForKey: @"targetType"];
        if(self.targetType && [self.targetType isEqual:[NSNull null]])
            self.targetType = nil;

        self.targetId = [jsonObject objectForKey: @"targetId"];
        if(self.targetId && [self.targetId isEqual:[NSNull null]])
            self.targetId = nil;

        self.contentCategory = [jsonObject objectForKey: @"contentCategory"];
        if(self.contentCategory && [self.contentCategory isEqual:[NSNull null]])
            self.contentCategory = nil;

        self.contact = [jsonObject objectForKey: @"contact"];
        if(self.contact && [self.contact isEqual:[NSNull null]])
            self.contact = nil;

        self.subject = [jsonObject objectForKey: @"subject"];
        if(self.subject && [self.subject isEqual:[NSNull null]])
            self.subject = nil;

        self.content = [jsonObject objectForKey: @"content"];
        if(self.content && [self.content isEqual:[NSNull null]])
            self.content = nil;

        self.proofResourceUri = [jsonObject objectForKey: @"proofResourceUri"];
        if(self.proofResourceUri && [self.proofResourceUri isEqual:[NSNull null]])
            self.proofResourceUri = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
