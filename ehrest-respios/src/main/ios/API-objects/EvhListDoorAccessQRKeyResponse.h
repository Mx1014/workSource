//
// EvhListDoorAccessQRKeyResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhDoorAccessQRKeyDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListDoorAccessQRKeyResponse
//
@interface EvhListDoorAccessQRKeyResponse
    : NSObject<EvhJsonSerializable>


// item type EvhDoorAccessQRKeyDTO*
@property(nonatomic, strong) NSMutableArray* keys;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

