//
// EvhRejectContactCommand.m
// generated at 2016-04-06 19:59:45 
//
#import "EvhRejectContactCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRejectContactCommand
//

@implementation EvhRejectContactCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRejectContactCommand* obj = [EvhRejectContactCommand new];
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
    if(self.enterpriseId)
        [jsonObject setObject: self.enterpriseId forKey: @"enterpriseId"];
    if(self.userId)
        [jsonObject setObject: self.userId forKey: @"userId"];
    if(self.rejectText)
        [jsonObject setObject: self.rejectText forKey: @"rejectText"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.enterpriseId = [jsonObject objectForKey: @"enterpriseId"];
        if(self.enterpriseId && [self.enterpriseId isEqual:[NSNull null]])
            self.enterpriseId = nil;

        self.userId = [jsonObject objectForKey: @"userId"];
        if(self.userId && [self.userId isEqual:[NSNull null]])
            self.userId = nil;

        self.rejectText = [jsonObject objectForKey: @"rejectText"];
        if(self.rejectText && [self.rejectText isEqual:[NSNull null]])
            self.rejectText = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
