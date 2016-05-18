//
// EvhCreateConfAccountOrderOnlineCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateConfAccountOrderOnlineCommand
//
@interface EvhCreateConfAccountOrderOnlineCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* enterpriseId;

@property(nonatomic, copy) NSString* enterpriseName;

@property(nonatomic, copy) NSString* contactor;

@property(nonatomic, copy) NSString* mobile;

@property(nonatomic, copy) NSNumber* buyChannel;

@property(nonatomic, copy) NSNumber* quantity;

@property(nonatomic, copy) NSNumber* period;

@property(nonatomic, copy) NSNumber* amount;

@property(nonatomic, copy) NSNumber* invoiceReqFlag;

@property(nonatomic, copy) NSNumber* confCapacity;

@property(nonatomic, copy) NSNumber* confType;

@property(nonatomic, copy) NSString* mailAddress;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

