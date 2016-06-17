//
// EvhDoorAccessCapapilityDTO.m
//
#import "EvhDoorAccessCapapilityDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDoorAccessCapapilityDTO
//

@implementation EvhDoorAccessCapapilityDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhDoorAccessCapapilityDTO* obj = [EvhDoorAccessCapapilityDTO new];
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
    if(self.isSupportQR)
        [jsonObject setObject: self.isSupportQR forKey: @"isSupportQR"];
    if(self.isSupportSmart)
        [jsonObject setObject: self.isSupportSmart forKey: @"isSupportSmart"];
    if(self.qrDriver)
        [jsonObject setObject: self.qrDriver forKey: @"qrDriver"];
    if(self.smartDriver)
        [jsonObject setObject: self.smartDriver forKey: @"smartDriver"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.isSupportQR = [jsonObject objectForKey: @"isSupportQR"];
        if(self.isSupportQR && [self.isSupportQR isEqual:[NSNull null]])
            self.isSupportQR = nil;

        self.isSupportSmart = [jsonObject objectForKey: @"isSupportSmart"];
        if(self.isSupportSmart && [self.isSupportSmart isEqual:[NSNull null]])
            self.isSupportSmart = nil;

        self.qrDriver = [jsonObject objectForKey: @"qrDriver"];
        if(self.qrDriver && [self.qrDriver isEqual:[NSNull null]])
            self.qrDriver = nil;

        self.smartDriver = [jsonObject objectForKey: @"smartDriver"];
        if(self.smartDriver && [self.smartDriver isEqual:[NSNull null]])
            self.smartDriver = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
