//
// EvhConfCategoryDTO.m
// generated at 2016-03-30 10:13:09 
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
    if(self.price)
        [jsonObject setObject: self.price forKey: @"price"];
    if(self.mutipleNum)
        [jsonObject setObject: self.mutipleNum forKey: @"mutipleNum"];
    if(self.mutiplePrice)
        [jsonObject setObject: self.mutiplePrice forKey: @"mutiplePrice"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.confCapacity = [jsonObject objectForKey: @"confCapacity"];
        if(self.confCapacity && [self.confCapacity isEqual:[NSNull null]])
            self.confCapacity = nil;

        self.price = [jsonObject objectForKey: @"price"];
        if(self.price && [self.price isEqual:[NSNull null]])
            self.price = nil;

        self.mutipleNum = [jsonObject objectForKey: @"mutipleNum"];
        if(self.mutipleNum && [self.mutipleNum isEqual:[NSNull null]])
            self.mutipleNum = nil;

        self.mutiplePrice = [jsonObject objectForKey: @"mutiplePrice"];
        if(self.mutiplePrice && [self.mutiplePrice isEqual:[NSNull null]])
            self.mutiplePrice = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
