//
// EvhFamilyBillingTransactionDTO.m
//
#import "EvhFamilyBillingTransactionDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFamilyBillingTransactionDTO
//

@implementation EvhFamilyBillingTransactionDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhFamilyBillingTransactionDTO* obj = [EvhFamilyBillingTransactionDTO new];
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
    if(self.behaviorTime)
        [jsonObject setObject: self.behaviorTime forKey: @"behaviorTime"];
    if(self.billType)
        [jsonObject setObject: self.billType forKey: @"billType"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
    if(self.chargeAmount)
        [jsonObject setObject: self.chargeAmount forKey: @"chargeAmount"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.behaviorTime = [jsonObject objectForKey: @"behaviorTime"];
        if(self.behaviorTime && [self.behaviorTime isEqual:[NSNull null]])
            self.behaviorTime = nil;

        self.billType = [jsonObject objectForKey: @"billType"];
        if(self.billType && [self.billType isEqual:[NSNull null]])
            self.billType = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        self.chargeAmount = [jsonObject objectForKey: @"chargeAmount"];
        if(self.chargeAmount && [self.chargeAmount isEqual:[NSNull null]])
            self.chargeAmount = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
