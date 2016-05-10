//
// EvhRejectPunchExceptionRequestCommand.m
//
#import "EvhRejectPunchExceptionRequestCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRejectPunchExceptionRequestCommand
//

@implementation EvhRejectPunchExceptionRequestCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRejectPunchExceptionRequestCommand* obj = [EvhRejectPunchExceptionRequestCommand new];
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
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.processDetail)
        [jsonObject setObject: self.processDetail forKey: @"processDetail"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.processDetail = [jsonObject objectForKey: @"processDetail"];
        if(self.processDetail && [self.processDetail isEqual:[NSNull null]])
            self.processDetail = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
