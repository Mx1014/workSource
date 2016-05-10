//
// EvhPunchClockCommand.m
//
#import "EvhPunchClockCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPunchClockCommand
//

@implementation EvhPunchClockCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPunchClockCommand* obj = [EvhPunchClockCommand new];
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
    if(self.identification)
        [jsonObject setObject: self.identification forKey: @"identification"];
    if(self.latitude)
        [jsonObject setObject: self.latitude forKey: @"latitude"];
    if(self.longitude)
        [jsonObject setObject: self.longitude forKey: @"longitude"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.enterpriseId = [jsonObject objectForKey: @"enterpriseId"];
        if(self.enterpriseId && [self.enterpriseId isEqual:[NSNull null]])
            self.enterpriseId = nil;

        self.identification = [jsonObject objectForKey: @"identification"];
        if(self.identification && [self.identification isEqual:[NSNull null]])
            self.identification = nil;

        self.latitude = [jsonObject objectForKey: @"latitude"];
        if(self.latitude && [self.latitude isEqual:[NSNull null]])
            self.latitude = nil;

        self.longitude = [jsonObject objectForKey: @"longitude"];
        if(self.longitude && [self.longitude isEqual:[NSNull null]])
            self.longitude = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
