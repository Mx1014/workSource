//
// EvhCardInfoDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCardInfoDTO
//
@interface EvhCardInfoDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* cardId;

@property(nonatomic, copy) NSString* mobile;

@property(nonatomic, copy) NSString* cardNo;

@property(nonatomic, copy) NSString* cardType;

@property(nonatomic, copy) NSNumber* activedTime;

@property(nonatomic, copy) NSNumber* expiredTime;

@property(nonatomic, copy) NSNumber* balance;

@property(nonatomic, copy) NSString* status;

@property(nonatomic, copy) NSString* vendorCardData;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

