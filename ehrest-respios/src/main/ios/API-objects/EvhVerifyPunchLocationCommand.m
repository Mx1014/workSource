//
// EvhVerifyPunchLocationCommand.m
//
#import "EvhVerifyPunchLocationCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhVerifyPunchLocationCommand
//

@implementation EvhVerifyPunchLocationCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhVerifyPunchLocationCommand* obj = [EvhVerifyPunchLocationCommand new];
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
    if(self.companyId)
        [jsonObject setObject: self.companyId forKey: @"companyId"];
    if(self.latitude)
        [jsonObject setObject: self.latitude forKey: @"latitude"];
    if(self.longitude)
        [jsonObject setObject: self.longitude forKey: @"longitude"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.companyId = [jsonObject objectForKey: @"companyId"];
        if(self.companyId && [self.companyId isEqual:[NSNull null]])
            self.companyId = nil;

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
