//
// EvhListStatisticsByDateDTO.m
//
#import "EvhListStatisticsByDateDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListStatisticsByDateDTO
//

@implementation EvhListStatisticsByDateDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListStatisticsByDateDTO* obj = [EvhListStatisticsByDateDTO new];
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
    if(self.createTime)
        [jsonObject setObject: self.createTime forKey: @"createTime"];
    if(self.registerConut)
        [jsonObject setObject: self.registerConut forKey: @"registerConut"];
    if(self.activeCount)
        [jsonObject setObject: self.activeCount forKey: @"activeCount"];
    if(self.regRatio)
        [jsonObject setObject: self.regRatio forKey: @"regRatio"];
    if(self.addressCount)
        [jsonObject setObject: self.addressCount forKey: @"addressCount"];
    if(self.totalRegisterCount)
        [jsonObject setObject: self.totalRegisterCount forKey: @"totalRegisterCount"];
    if(self.addrRatio)
        [jsonObject setObject: self.addrRatio forKey: @"addrRatio"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.createTime = [jsonObject objectForKey: @"createTime"];
        if(self.createTime && [self.createTime isEqual:[NSNull null]])
            self.createTime = nil;

        self.registerConut = [jsonObject objectForKey: @"registerConut"];
        if(self.registerConut && [self.registerConut isEqual:[NSNull null]])
            self.registerConut = nil;

        self.activeCount = [jsonObject objectForKey: @"activeCount"];
        if(self.activeCount && [self.activeCount isEqual:[NSNull null]])
            self.activeCount = nil;

        self.regRatio = [jsonObject objectForKey: @"regRatio"];
        if(self.regRatio && [self.regRatio isEqual:[NSNull null]])
            self.regRatio = nil;

        self.addressCount = [jsonObject objectForKey: @"addressCount"];
        if(self.addressCount && [self.addressCount isEqual:[NSNull null]])
            self.addressCount = nil;

        self.totalRegisterCount = [jsonObject objectForKey: @"totalRegisterCount"];
        if(self.totalRegisterCount && [self.totalRegisterCount isEqual:[NSNull null]])
            self.totalRegisterCount = nil;

        self.addrRatio = [jsonObject objectForKey: @"addrRatio"];
        if(self.addrRatio && [self.addrRatio isEqual:[NSNull null]])
            self.addrRatio = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
