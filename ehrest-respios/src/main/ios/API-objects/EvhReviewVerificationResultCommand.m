//
// EvhReviewVerificationResultCommand.m
//
#import "EvhReviewVerificationResultCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhReviewVerificationResultCommand
//

@implementation EvhReviewVerificationResultCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhReviewVerificationResultCommand* obj = [EvhReviewVerificationResultCommand new];
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
    if(self.taskId)
        [jsonObject setObject: self.taskId forKey: @"taskId"];
    if(self.reviewResult)
        [jsonObject setObject: self.reviewResult forKey: @"reviewResult"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.taskId = [jsonObject objectForKey: @"taskId"];
        if(self.taskId && [self.taskId isEqual:[NSNull null]])
            self.taskId = nil;

        self.reviewResult = [jsonObject objectForKey: @"reviewResult"];
        if(self.reviewResult && [self.reviewResult isEqual:[NSNull null]])
            self.reviewResult = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
