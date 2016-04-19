//
// EvhBaiduAddressComponentDTO.h
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBaiduAddressComponentDTO
//
@interface EvhBaiduAddressComponentDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* country;

@property(nonatomic, copy) NSString* province;

@property(nonatomic, copy) NSString* city;

@property(nonatomic, copy) NSString* district;

@property(nonatomic, copy) NSString* street;

@property(nonatomic, copy) NSString* street_number;

@property(nonatomic, copy) NSString* adcode;

@property(nonatomic, copy) NSString* country_code;

@property(nonatomic, copy) NSString* direction;

@property(nonatomic, copy) NSString* distance;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

