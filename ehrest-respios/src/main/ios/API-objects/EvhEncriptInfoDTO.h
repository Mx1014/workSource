//
// EvhEncriptInfoDTO.h
// generated at 2016-04-07 17:03:17 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhEncriptInfoDTO
//
@interface EvhEncriptInfoDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* plainText;

@property(nonatomic, copy) NSString* plainTextHash;

@property(nonatomic, copy) NSString* salt;

@property(nonatomic, copy) NSString* encryptText;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

