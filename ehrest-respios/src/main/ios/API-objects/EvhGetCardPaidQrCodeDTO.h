//
// EvhGetCardPaidQrCodeDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetCardPaidQrCodeDTO
//
@interface EvhGetCardPaidQrCodeDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* code;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

