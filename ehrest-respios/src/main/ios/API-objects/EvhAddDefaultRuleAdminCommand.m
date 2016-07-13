//
// EvhAddDefaultRuleAdminCommand.m
//
#import "EvhAddDefaultRuleAdminCommand.h"
#import "EvhAttachmentConfigDTO.h"
#import "EvhTimeIntervalDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddDefaultRuleAdminCommand
//

@implementation EvhAddDefaultRuleAdminCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhAddDefaultRuleAdminCommand* obj = [EvhAddDefaultRuleAdminCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _attachments = [NSMutableArray new];
        _timeIntervals = [NSMutableArray new];
        _openWeekday = [NSMutableArray new];
        _closeDates = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.ownerType)
        [jsonObject setObject: self.ownerType forKey: @"ownerType"];
    if(self.ownerId)
        [jsonObject setObject: self.ownerId forKey: @"ownerId"];
    if(self.launchPadItemId)
        [jsonObject setObject: self.launchPadItemId forKey: @"launchPadItemId"];
    if(self.exclusiveFlag)
        [jsonObject setObject: self.exclusiveFlag forKey: @"exclusiveFlag"];
    if(self.unit)
        [jsonObject setObject: self.unit forKey: @"unit"];
    if(self.autoAssign)
        [jsonObject setObject: self.autoAssign forKey: @"autoAssign"];
    if(self.multiUnit)
        [jsonObject setObject: self.multiUnit forKey: @"multiUnit"];
    if(self.needPay)
        [jsonObject setObject: self.needPay forKey: @"needPay"];
    if(self.multiTimeInterval)
        [jsonObject setObject: self.multiTimeInterval forKey: @"multiTimeInterval"];
    if(self.attachments) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhAttachmentConfigDTO* item in self.attachments) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"attachments"];
    }
    if(self.rentalType)
        [jsonObject setObject: self.rentalType forKey: @"rentalType"];
    if(self.rentalEndTime)
        [jsonObject setObject: self.rentalEndTime forKey: @"rentalEndTime"];
    if(self.rentalStartTime)
        [jsonObject setObject: self.rentalStartTime forKey: @"rentalStartTime"];
    if(self.timeStep)
        [jsonObject setObject: self.timeStep forKey: @"timeStep"];
    if(self.timeIntervals) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhTimeIntervalDTO* item in self.timeIntervals) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"timeIntervals"];
    }
    if(self.beginDate)
        [jsonObject setObject: self.beginDate forKey: @"beginDate"];
    if(self.endDate)
        [jsonObject setObject: self.endDate forKey: @"endDate"];
    if(self.openWeekday) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSNumber* item in self.openWeekday) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"openWeekday"];
    }
    if(self.closeDates) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSNumber* item in self.closeDates) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"closeDates"];
    }
    if(self.workdayPrice)
        [jsonObject setObject: self.workdayPrice forKey: @"workdayPrice"];
    if(self.weekendPrice)
        [jsonObject setObject: self.weekendPrice forKey: @"weekendPrice"];
    if(self.siteCounts)
        [jsonObject setObject: self.siteCounts forKey: @"siteCounts"];
    if(self.cancelTime)
        [jsonObject setObject: self.cancelTime forKey: @"cancelTime"];
    if(self.refundFlag)
        [jsonObject setObject: self.refundFlag forKey: @"refundFlag"];
    if(self.refundRatio)
        [jsonObject setObject: self.refundRatio forKey: @"refundRatio"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.ownerType = [jsonObject objectForKey: @"ownerType"];
        if(self.ownerType && [self.ownerType isEqual:[NSNull null]])
            self.ownerType = nil;

        self.ownerId = [jsonObject objectForKey: @"ownerId"];
        if(self.ownerId && [self.ownerId isEqual:[NSNull null]])
            self.ownerId = nil;

        self.launchPadItemId = [jsonObject objectForKey: @"launchPadItemId"];
        if(self.launchPadItemId && [self.launchPadItemId isEqual:[NSNull null]])
            self.launchPadItemId = nil;

        self.exclusiveFlag = [jsonObject objectForKey: @"exclusiveFlag"];
        if(self.exclusiveFlag && [self.exclusiveFlag isEqual:[NSNull null]])
            self.exclusiveFlag = nil;

        self.unit = [jsonObject objectForKey: @"unit"];
        if(self.unit && [self.unit isEqual:[NSNull null]])
            self.unit = nil;

        self.autoAssign = [jsonObject objectForKey: @"autoAssign"];
        if(self.autoAssign && [self.autoAssign isEqual:[NSNull null]])
            self.autoAssign = nil;

        self.multiUnit = [jsonObject objectForKey: @"multiUnit"];
        if(self.multiUnit && [self.multiUnit isEqual:[NSNull null]])
            self.multiUnit = nil;

        self.needPay = [jsonObject objectForKey: @"needPay"];
        if(self.needPay && [self.needPay isEqual:[NSNull null]])
            self.needPay = nil;

        self.multiTimeInterval = [jsonObject objectForKey: @"multiTimeInterval"];
        if(self.multiTimeInterval && [self.multiTimeInterval isEqual:[NSNull null]])
            self.multiTimeInterval = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"attachments"];
            for(id itemJson in jsonArray) {
                EvhAttachmentConfigDTO* item = [EvhAttachmentConfigDTO new];
                
                [item fromJson: itemJson];
                [self.attachments addObject: item];
            }
        }
        self.rentalType = [jsonObject objectForKey: @"rentalType"];
        if(self.rentalType && [self.rentalType isEqual:[NSNull null]])
            self.rentalType = nil;

        self.rentalEndTime = [jsonObject objectForKey: @"rentalEndTime"];
        if(self.rentalEndTime && [self.rentalEndTime isEqual:[NSNull null]])
            self.rentalEndTime = nil;

        self.rentalStartTime = [jsonObject objectForKey: @"rentalStartTime"];
        if(self.rentalStartTime && [self.rentalStartTime isEqual:[NSNull null]])
            self.rentalStartTime = nil;

        self.timeStep = [jsonObject objectForKey: @"timeStep"];
        if(self.timeStep && [self.timeStep isEqual:[NSNull null]])
            self.timeStep = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"timeIntervals"];
            for(id itemJson in jsonArray) {
                EvhTimeIntervalDTO* item = [EvhTimeIntervalDTO new];
                
                [item fromJson: itemJson];
                [self.timeIntervals addObject: item];
            }
        }
        self.beginDate = [jsonObject objectForKey: @"beginDate"];
        if(self.beginDate && [self.beginDate isEqual:[NSNull null]])
            self.beginDate = nil;

        self.endDate = [jsonObject objectForKey: @"endDate"];
        if(self.endDate && [self.endDate isEqual:[NSNull null]])
            self.endDate = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"openWeekday"];
            for(id itemJson in jsonArray) {
                [self.openWeekday addObject: itemJson];
            }
        }
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"closeDates"];
            for(id itemJson in jsonArray) {
                [self.closeDates addObject: itemJson];
            }
        }
        self.workdayPrice = [jsonObject objectForKey: @"workdayPrice"];
        if(self.workdayPrice && [self.workdayPrice isEqual:[NSNull null]])
            self.workdayPrice = nil;

        self.weekendPrice = [jsonObject objectForKey: @"weekendPrice"];
        if(self.weekendPrice && [self.weekendPrice isEqual:[NSNull null]])
            self.weekendPrice = nil;

        self.siteCounts = [jsonObject objectForKey: @"siteCounts"];
        if(self.siteCounts && [self.siteCounts isEqual:[NSNull null]])
            self.siteCounts = nil;

        self.cancelTime = [jsonObject objectForKey: @"cancelTime"];
        if(self.cancelTime && [self.cancelTime isEqual:[NSNull null]])
            self.cancelTime = nil;

        self.refundFlag = [jsonObject objectForKey: @"refundFlag"];
        if(self.refundFlag && [self.refundFlag isEqual:[NSNull null]])
            self.refundFlag = nil;

        self.refundRatio = [jsonObject objectForKey: @"refundRatio"];
        if(self.refundRatio && [self.refundRatio isEqual:[NSNull null]])
            self.refundRatio = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
