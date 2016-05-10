//
// EvhRechargeRecordDTO.m
//
#import "EvhRechargeRecordDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRechargeRecordDTO
//

@implementation EvhRechargeRecordDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRechargeRecordDTO* obj = [EvhRechargeRecordDTO new];
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
    if(self.plateNumber)
        [jsonObject setObject: self.plateNumber forKey: @"plateNumber"];
    if(self.ownerName)
        [jsonObject setObject: self.ownerName forKey: @"ownerName"];
    if(self.rechargePhone)
        [jsonObject setObject: self.rechargePhone forKey: @"rechargePhone"];
    if(self.rechargeTime)
        [jsonObject setObject: self.rechargeTime forKey: @"rechargeTime"];
    if(self.rechargeMonth)
        [jsonObject setObject: self.rechargeMonth forKey: @"rechargeMonth"];
    if(self.rechargeAmount)
        [jsonObject setObject: self.rechargeAmount forKey: @"rechargeAmount"];
    if(self.paymentStatus)
        [jsonObject setObject: self.paymentStatus forKey: @"paymentStatus"];
    if(self.rechargeStatus)
        [jsonObject setObject: self.rechargeStatus forKey: @"rechargeStatus"];
    if(self.validityperiod)
        [jsonObject setObject: self.validityperiod forKey: @"validityperiod"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.plateNumber = [jsonObject objectForKey: @"plateNumber"];
        if(self.plateNumber && [self.plateNumber isEqual:[NSNull null]])
            self.plateNumber = nil;

        self.ownerName = [jsonObject objectForKey: @"ownerName"];
        if(self.ownerName && [self.ownerName isEqual:[NSNull null]])
            self.ownerName = nil;

        self.rechargePhone = [jsonObject objectForKey: @"rechargePhone"];
        if(self.rechargePhone && [self.rechargePhone isEqual:[NSNull null]])
            self.rechargePhone = nil;

        self.rechargeTime = [jsonObject objectForKey: @"rechargeTime"];
        if(self.rechargeTime && [self.rechargeTime isEqual:[NSNull null]])
            self.rechargeTime = nil;

        self.rechargeMonth = [jsonObject objectForKey: @"rechargeMonth"];
        if(self.rechargeMonth && [self.rechargeMonth isEqual:[NSNull null]])
            self.rechargeMonth = nil;

        self.rechargeAmount = [jsonObject objectForKey: @"rechargeAmount"];
        if(self.rechargeAmount && [self.rechargeAmount isEqual:[NSNull null]])
            self.rechargeAmount = nil;

        self.paymentStatus = [jsonObject objectForKey: @"paymentStatus"];
        if(self.paymentStatus && [self.paymentStatus isEqual:[NSNull null]])
            self.paymentStatus = nil;

        self.rechargeStatus = [jsonObject objectForKey: @"rechargeStatus"];
        if(self.rechargeStatus && [self.rechargeStatus isEqual:[NSNull null]])
            self.rechargeStatus = nil;

        self.validityperiod = [jsonObject objectForKey: @"validityperiod"];
        if(self.validityperiod && [self.validityperiod isEqual:[NSNull null]])
            self.validityperiod = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
