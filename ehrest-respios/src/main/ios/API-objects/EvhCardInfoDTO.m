//
// EvhCardInfoDTO.m
//
#import "EvhCardInfoDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCardInfoDTO
//

@implementation EvhCardInfoDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCardInfoDTO* obj = [EvhCardInfoDTO new];
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
    if(self.cardId)
        [jsonObject setObject: self.cardId forKey: @"cardId"];
    if(self.mobile)
        [jsonObject setObject: self.mobile forKey: @"mobile"];
    if(self.cardNo)
        [jsonObject setObject: self.cardNo forKey: @"cardNo"];
    if(self.cardType)
        [jsonObject setObject: self.cardType forKey: @"cardType"];
    if(self.activedTime)
        [jsonObject setObject: self.activedTime forKey: @"activedTime"];
    if(self.expiredTime)
        [jsonObject setObject: self.expiredTime forKey: @"expiredTime"];
    if(self.balance)
        [jsonObject setObject: self.balance forKey: @"balance"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.vendorCardData)
        [jsonObject setObject: self.vendorCardData forKey: @"vendorCardData"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.cardId = [jsonObject objectForKey: @"cardId"];
        if(self.cardId && [self.cardId isEqual:[NSNull null]])
            self.cardId = nil;

        self.mobile = [jsonObject objectForKey: @"mobile"];
        if(self.mobile && [self.mobile isEqual:[NSNull null]])
            self.mobile = nil;

        self.cardNo = [jsonObject objectForKey: @"cardNo"];
        if(self.cardNo && [self.cardNo isEqual:[NSNull null]])
            self.cardNo = nil;

        self.cardType = [jsonObject objectForKey: @"cardType"];
        if(self.cardType && [self.cardType isEqual:[NSNull null]])
            self.cardType = nil;

        self.activedTime = [jsonObject objectForKey: @"activedTime"];
        if(self.activedTime && [self.activedTime isEqual:[NSNull null]])
            self.activedTime = nil;

        self.expiredTime = [jsonObject objectForKey: @"expiredTime"];
        if(self.expiredTime && [self.expiredTime isEqual:[NSNull null]])
            self.expiredTime = nil;

        self.balance = [jsonObject objectForKey: @"balance"];
        if(self.balance && [self.balance isEqual:[NSNull null]])
            self.balance = nil;

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.vendorCardData = [jsonObject objectForKey: @"vendorCardData"];
        if(self.vendorCardData && [self.vendorCardData isEqual:[NSNull null]])
            self.vendorCardData = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
