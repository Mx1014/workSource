//
// EvhAddPunchExceptionRequestCommand.m
//
#import "EvhAddPunchExceptionRequestCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddPunchExceptionRequestCommand
//

@implementation EvhAddPunchExceptionRequestCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhAddPunchExceptionRequestCommand* obj = [EvhAddPunchExceptionRequestCommand new];
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
    if(self.punchDate)
        [jsonObject setObject: self.punchDate forKey: @"punchDate"];
    if(self.requestDescription)
        [jsonObject setObject: self.requestDescription forKey: @"requestDescription"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.enterpriseId = [jsonObject objectForKey: @"enterpriseId"];
        if(self.enterpriseId && [self.enterpriseId isEqual:[NSNull null]])
            self.enterpriseId = nil;

        self.punchDate = [jsonObject objectForKey: @"punchDate"];
        if(self.punchDate && [self.punchDate isEqual:[NSNull null]])
            self.punchDate = nil;

        self.requestDescription = [jsonObject objectForKey: @"requestDescription"];
        if(self.requestDescription && [self.requestDescription isEqual:[NSNull null]])
            self.requestDescription = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
