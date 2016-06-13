//
// EvhConfCategoryDTO.m
//
#import "EvhConfCategoryDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhConfCategoryDTO
//

@implementation EvhConfCategoryDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhConfCategoryDTO* obj = [EvhConfCategoryDTO new];
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
    if(self.confCapacity)
        [jsonObject setObject: self.confCapacity forKey: @"confCapacity"];
    if(self.singleAccountPrice)
        [jsonObject setObject: self.singleAccountPrice forKey: @"singleAccountPrice"];
    if(self.multipleAccountThreshold)
        [jsonObject setObject: self.multipleAccountThreshold forKey: @"multipleAccountThreshold"];
    if(self.multipleAccountPrice)
        [jsonObject setObject: self.multipleAccountPrice forKey: @"multipleAccountPrice"];
    if(self.minPeriod)
        [jsonObject setObject: self.minPeriod forKey: @"minPeriod"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.confCapacity = [jsonObject objectForKey: @"confCapacity"];
        if(self.confCapacity && [self.confCapacity isEqual:[NSNull null]])
            self.confCapacity = nil;

        self.singleAccountPrice = [jsonObject objectForKey: @"singleAccountPrice"];
        if(self.singleAccountPrice && [self.singleAccountPrice isEqual:[NSNull null]])
            self.singleAccountPrice = nil;

        self.multipleAccountThreshold = [jsonObject objectForKey: @"multipleAccountThreshold"];
        if(self.multipleAccountThreshold && [self.multipleAccountThreshold isEqual:[NSNull null]])
            self.multipleAccountThreshold = nil;

        self.multipleAccountPrice = [jsonObject objectForKey: @"multipleAccountPrice"];
        if(self.multipleAccountPrice && [self.multipleAccountPrice isEqual:[NSNull null]])
            self.multipleAccountPrice = nil;

        self.minPeriod = [jsonObject objectForKey: @"minPeriod"];
        if(self.minPeriod && [self.minPeriod isEqual:[NSNull null]])
            self.minPeriod = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
