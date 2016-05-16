//
// EvhRefreshParkingSystemResponse.m
//
#import "EvhRefreshParkingSystemResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRefreshParkingSystemResponse
//

@implementation EvhRefreshParkingSystemResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRefreshParkingSystemResponse* obj = [EvhRefreshParkingSystemResponse new];
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
    if(self.carNumber)
        [jsonObject setObject: self.carNumber forKey: @"carNumber"];
    if(self.flag)
        [jsonObject setObject: self.flag forKey: @"flag"];
    if(self.cost)
        [jsonObject setObject: self.cost forKey: @"cost"];
    if(self.validStart)
        [jsonObject setObject: self.validStart forKey: @"validStart"];
    if(self.validEnd)
        [jsonObject setObject: self.validEnd forKey: @"validEnd"];
    if(self.payTime)
        [jsonObject setObject: self.payTime forKey: @"payTime"];
    if(self.sign)
        [jsonObject setObject: self.sign forKey: @"sign"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.carNumber = [jsonObject objectForKey: @"carNumber"];
        if(self.carNumber && [self.carNumber isEqual:[NSNull null]])
            self.carNumber = nil;

        self.flag = [jsonObject objectForKey: @"flag"];
        if(self.flag && [self.flag isEqual:[NSNull null]])
            self.flag = nil;

        self.cost = [jsonObject objectForKey: @"cost"];
        if(self.cost && [self.cost isEqual:[NSNull null]])
            self.cost = nil;

        self.validStart = [jsonObject objectForKey: @"validStart"];
        if(self.validStart && [self.validStart isEqual:[NSNull null]])
            self.validStart = nil;

        self.validEnd = [jsonObject objectForKey: @"validEnd"];
        if(self.validEnd && [self.validEnd isEqual:[NSNull null]])
            self.validEnd = nil;

        self.payTime = [jsonObject objectForKey: @"payTime"];
        if(self.payTime && [self.payTime isEqual:[NSNull null]])
            self.payTime = nil;

        self.sign = [jsonObject objectForKey: @"sign"];
        if(self.sign && [self.sign isEqual:[NSNull null]])
            self.sign = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
